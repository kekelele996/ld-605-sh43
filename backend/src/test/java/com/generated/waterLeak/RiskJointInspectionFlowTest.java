package com.generated.waterLeak;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 风险联巡 → 维修验收全流程集成测试：真实 PostgreSQL（内嵌）+ 真实 init.sql。
 * 覆盖：核查队列排序、BURST 未核实禁派工、同队同日一单（含并发）、领料验收同事务回滚。
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(EmbeddedPostgresConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RiskJointInspectionFlowTest {

  @Autowired
  TestRestTemplate http;

  private static final ParameterizedTypeReference<List<Map<String, Object>>> LIST_OF_MAP =
      new ParameterizedTypeReference<>() {};

  private List<Map<String, Object>> getList(String path) {
    ResponseEntity<List<Map<String, Object>>> res =
        http.exchange(path, HttpMethod.GET, null, LIST_OF_MAP);
    assertEquals(HttpStatus.OK, res.getStatusCode());
    return res.getBody();
  }

  @Test
  @Order(1)
  void queueSortedByLeakLevelRiskThenOverdue() {
    List<Map<String, Object>> queue = getList("/api/leak-reports/verification-queue");
    assertEquals(5, queue.size());
    // BURST/EXTREME/超期13天 → BURST/HIGH → MAJOR/MEDIUM → MINOR/LOW → TRACE/EXTREME
    assertEquals(List.of(1, 2, 3, 4, 5),
        queue.stream().map(row -> ((Number) row.get("report_id")).intValue()).toList());

    Map<String, Object> first = queue.get(0);
    assertEquals("BURST", first.get("leak_level"));
    assertEquals("EXTREME", first.get("risk_level"));
    assertEquals(13L, ((Number) first.get("overdue_days")).longValue());
    assertEquals(false, first.get("dispatchable"));
    assertEquals("BURST_UNVERIFIED", first.get("blocked_code"));
    assertNotNull(first.get("blocked_reason"));

    // 第三条（MAJOR）种子数据里已有未关闭维修单
    assertEquals("ORDER_ALREADY_OPEN", queue.get(2).get("blocked_code"));
    // 非 BURST 未核实不阻塞派工
    assertEquals(true, queue.get(3).get("dispatchable"));
  }

  @Test
  @Order(2)
  void burstUnverifiedCannotDispatch() {
    ResponseEntity<Map<String, Object>> res = http.exchange("/api/repair-orders/dispatch",
        HttpMethod.POST, new HttpEntity<>(Map.of("leakReportId", 1, "crewId", 2)),
        new ParameterizedTypeReference<>() {});
    assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    assertEquals("BURST_UNVERIFIED", res.getBody().get("code"));
  }

  @Test
  @Order(3)
  void crewDayConflictWhenCrewHasOpenOrder() {
    // 种子数据：抢修一队（crew 1）今天已有一张未关闭维修单
    ResponseEntity<Map<String, Object>> res = http.exchange("/api/repair-orders/dispatch",
        HttpMethod.POST, new HttpEntity<>(Map.of("leakReportId", 2, "crewId", 1)),
        new ParameterizedTypeReference<>() {});
    assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    assertEquals("CREW_DAY_CONFLICT", res.getBody().get("code"));
  }

  @Test
  @Order(4)
  void concurrentDispatchOnlyOneSucceeds() throws Exception {
    // 报告 2 与报告 5 同时派给抢修二队（crew 2）今天：只能成功一单
    Callable<ResponseEntity<Map<String, Object>>> dispatchReport2 = () -> http.exchange(
        "/api/repair-orders/dispatch", HttpMethod.POST,
        new HttpEntity<>(Map.of("leakReportId", 2, "crewId", 2)),
        new ParameterizedTypeReference<>() {});
    Callable<ResponseEntity<Map<String, Object>>> dispatchReport5 = () -> http.exchange(
        "/api/repair-orders/dispatch", HttpMethod.POST,
        new HttpEntity<>(Map.of("leakReportId", 5, "crewId", 2)),
        new ParameterizedTypeReference<>() {});

    var pool = Executors.newFixedThreadPool(2);
    try {
      Future<ResponseEntity<Map<String, Object>>> a = pool.submit(dispatchReport2);
      Future<ResponseEntity<Map<String, Object>>> b = pool.submit(dispatchReport5);
      List<ResponseEntity<Map<String, Object>>> results = List.of(a.get(), b.get());
      long ok = results.stream().filter(r -> r.getStatusCode() == HttpStatus.OK).count();
      long conflict = results.stream()
          .filter(r -> r.getStatusCode() == HttpStatus.CONFLICT
              && "CREW_DAY_CONFLICT".equals(r.getBody().get("code")))
          .count();
      assertEquals(1, ok, "并发派工只成功一单");
      assertEquals(1, conflict, "另一单必须返回 CREW_DAY_CONFLICT");
    } finally {
      pool.shutdownNow();
    }
  }

  @Test
  @Order(5)
  void acceptanceFailureRollsBackPickupAndKeepsOrderOpen() {
    // 找一个仍可派工的报告（第 4 步并发派工后，报告 2/5 中恰有一个仍无未关闭维修单）
    Long reportId = getList("/api/leak-reports/verification-queue").stream()
        .filter(row -> Boolean.TRUE.equals(row.get("dispatchable")))
        .map(row -> ((Number) row.get("report_id")).longValue())
        .findFirst().orElseThrow();
    // 派给抢修三队（crew 3）今天
    ResponseEntity<Map<String, Object>> dispatched = http.exchange("/api/repair-orders/dispatch",
        HttpMethod.POST, new HttpEntity<>(Map.of("leakReportId", reportId, "crewId", 3)),
        new ParameterizedTypeReference<>() {});
    assertEquals(HttpStatus.OK, dispatched.getStatusCode());
    long orderId = ((Number) dispatched.getBody().get("id")).longValue();

    int stockBefore = stockOf("WH-01", "MAT-PIPE-DN300");

    // 验收不通过：领料与验收同事务回滚
    Map<String, Object> failBody = Map.of(
        "materials", List.of(Map.of("materialCode", "MAT-PIPE-DN300", "warehouse", "WH-01", "quantity", 2)),
        "acceptancePassed", false);
    ResponseEntity<Map<String, Object>> failed = http.exchange(
        "/api/repair-orders/" + orderId + "/accept", HttpMethod.POST,
        new HttpEntity<>(failBody), new ParameterizedTypeReference<>() {});
    assertEquals(HttpStatus.CONFLICT, failed.getStatusCode());
    assertEquals("ACCEPTANCE_FAILED", failed.getBody().get("code"));

    // 不扣料、不写流水、不关单
    assertEquals(stockBefore, stockOf("WH-01", "MAT-PIPE-DN300"));
    assertTrue(getList("/api/material-usages?orderId=" + orderId).isEmpty());
    assertEquals("ASSIGNED", orderOf(orderId).get("status"));

    // 库存不足同样整体回滚
    Map<String, Object> tooMuch = Map.of(
        "materials", List.of(Map.of("materialCode", "MAT-PIPE-DN300", "warehouse", "WH-01", "quantity", 999)),
        "acceptancePassed", true);
    ResponseEntity<Map<String, Object>> insufficient = http.exchange(
        "/api/repair-orders/" + orderId + "/accept", HttpMethod.POST,
        new HttpEntity<>(tooMuch), new ParameterizedTypeReference<>() {});
    assertEquals(HttpStatus.CONFLICT, insufficient.getStatusCode());
    assertEquals("INSUFFICIENT_STOCK", insufficient.getBody().get("code"));
    assertEquals(stockBefore, stockOf("WH-01", "MAT-PIPE-DN300"));

    // 验收通过：扣料 2、流水 CONSUMED、关单并记费用
    Map<String, Object> passBody = Map.of(
        "materials", List.of(Map.of("materialCode", "MAT-PIPE-DN300", "warehouse", "WH-01", "quantity", 2)),
        "acceptancePassed", true,
        "costAmount", 8600);
    ResponseEntity<Map<String, Object>> passed = http.exchange(
        "/api/repair-orders/" + orderId + "/accept", HttpMethod.POST,
        new HttpEntity<>(passBody), new ParameterizedTypeReference<>() {});
    assertEquals(HttpStatus.OK, passed.getStatusCode());
    assertEquals("CLOSED", passed.getBody().get("status"));
    assertEquals(stockBefore - 2, stockOf("WH-01", "MAT-PIPE-DN300"));

    List<Map<String, Object>> usages = getList("/api/material-usages?orderId=" + orderId);
    assertEquals(1, usages.size());
    assertEquals("CONSUMED", usages.get(0).get("usage_status"));

    Map<String, Object> closed = orderOf(orderId);
    assertEquals("CLOSED", closed.get("status"));
    assertNotNull(closed.get("finished_at"));

    // 已关闭维修单不允许重复验收
    ResponseEntity<Map<String, Object>> again = http.exchange(
        "/api/repair-orders/" + orderId + "/accept", HttpMethod.POST,
        new HttpEntity<>(passBody), new ParameterizedTypeReference<>() {});
    assertEquals(HttpStatus.CONFLICT, again.getStatusCode());
    assertEquals("ORDER_STATE_INVALID", again.getBody().get("code"));
  }

  @Test
  @Order(6)
  void reportAndVerifyFlowPersists() {
    // 上报 → 默认 PENDING → 核实 VERIFIED → 队列可派工
    Map<String, Object> reportBody = Map.of(
        "pointId", 4, "leakLevel", "BURST", "reporterType", "INSPECTOR", "description", "集成测试上报");
    ResponseEntity<Map<String, Object>> reported = http.exchange("/api/leak-reports",
        HttpMethod.POST, new HttpEntity<>(reportBody), new ParameterizedTypeReference<>() {});
    assertEquals(HttpStatus.OK, reported.getStatusCode());
    long reportId = ((Number) reported.getBody().get("id")).longValue();
    assertEquals("PENDING", reported.getBody().get("verify_status"));

    // BURST 未核实不能派工
    ResponseEntity<Map<String, Object>> blocked = http.exchange("/api/repair-orders/dispatch",
        HttpMethod.POST, new HttpEntity<>(Map.of("leakReportId", reportId, "crewId", 3)),
        new ParameterizedTypeReference<>() {});
    assertEquals("BURST_UNVERIFIED", blocked.getBody().get("code"));

    http.exchange("/api/leak-reports/" + reportId + "/verify", HttpMethod.POST,
        new HttpEntity<>(Map.of("verifyStatus", "VERIFIED")), new ParameterizedTypeReference<>() {});

    Map<String, Object> row = getList("/api/leak-reports/verification-queue").stream()
        .filter(r -> ((Number) r.get("report_id")).longValue() == reportId)
        .findFirst().orElseThrow();
    assertEquals("VERIFIED", row.get("verify_status"));
    assertEquals(true, row.get("dispatchable"));
    assertNull(row.get("blocked_reason"));
  }

  private int stockOf(String warehouse, String materialCode) {
    return getList("/api/material-stocks").stream()
        .filter(s -> warehouse.equals(s.get("warehouse")) && materialCode.equals(s.get("materialCode")))
        .map(s -> ((Number) s.get("quantity")).intValue())
        .findFirst().orElseThrow();
  }

  private Map<String, Object> orderOf(long orderId) {
    return getList("/api/repair-orders").stream()
        .filter(o -> ((Number) o.get("id")).longValue() == orderId)
        .findFirst().orElseThrow();
  }
}
