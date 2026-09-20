package com.generated.waterLeak.controllers;

import com.generated.waterLeak.routes.LeakReportRoutes;
import com.generated.waterLeak.services.LeakReportService;
import com.generated.waterLeak.types.BizException;
import com.generated.waterLeak.types.LeakReportPayload;
import com.generated.waterLeak.types.LeakReportVerifyPayload;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(LeakReportRoutes.PATH)
public class LeakReportController {
  private static final Logger log = LoggerFactory.getLogger(LeakReportController.class);

  private final LeakReportService service;

  public LeakReportController(LeakReportService service) { this.service = service; }

  /** 漏损核查列表：BURST→TRACE、管段风险高→低、同分看巡检超期。 */
  @GetMapping
  public List<Map<String, Object>> list() {
    return service.list();
  }

  /** 上报。 */
  @PostMapping
  public Map<String, Object> create(@Valid @RequestBody LeakReportPayload payload) {
    try {
      return service.create(payload);
    } catch (BizException e) {
      log.warn("leak report create rejected: code={} message={}", e.getCode(), e.getMessage());
      throw e;
    }
  }

  /** 核实。 */
  @PostMapping(LeakReportRoutes.VERIFY)
  public Map<String, Object> verify(@PathVariable Long id, @Valid @RequestBody LeakReportVerifyPayload payload) {
    try {
      return service.verify(id, payload.result());
    } catch (BizException e) {
      log.warn("leak report verify rejected: id={} code={} message={}", id, e.getCode(), e.getMessage());
      throw e;
    }
  }
}
