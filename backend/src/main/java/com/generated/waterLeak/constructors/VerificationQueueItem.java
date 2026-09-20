package com.generated.waterLeak.constructors;

import com.generated.waterLeak.constants.CheckFrequency;
import com.generated.waterLeak.constants.ErrorCodes;
import com.generated.waterLeak.constants.ErrorMessages;
import com.generated.waterLeak.constants.LeakLevel;
import com.generated.waterLeak.constants.RiskLevel;
import com.generated.waterLeak.constants.VerifyStatus;
import com.generated.waterLeak.models.InspectionPoint;
import com.generated.waterLeak.models.LeakReport;
import com.generated.waterLeak.models.PipelineSegment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 漏损核查队列行：按 漏损级别(BURST→TRACE) → 管段风险(EXTREME→LOW) → 巡检超期天数 排序，
 * 并携带派工阻塞原因，刷新页面后由服务端重新计算，保证状态一致。
 */
public record VerificationQueueItem(
    Long reportId,
    Long pointId,
    String pointCode,
    Long segmentId,
    String segmentCode,
    String district,
    String leakLevel,
    String verifyStatus,
    String description,
    LocalDateTime reportedAt,
    String riskLevel,
    String checkFrequency,
    LocalDateTime lastCheckedAt,
    long overdueDays,
    boolean dispatchable,
    String blockedCode,
    String blockedReason,
    Long openOrderId) {

  public static VerificationQueueItem of(LeakReport report, InspectionPoint point, PipelineSegment segment, Long openOrderId) {
    long overdueDays = computeOverdueDays(point);
    String blockedCode = blockedCodeOf(report, openOrderId);
    return new VerificationQueueItem(
        report.id,
        report.pointId,
        point == null ? null : point.pointCode,
        segment == null ? null : segment.id,
        segment == null ? null : segment.segmentCode,
        segment == null ? null : segment.district,
        report.leakLevel,
        report.verifyStatus,
        report.description,
        report.reportedAt,
        segment == null ? null : segment.riskLevel,
        point == null ? null : point.checkFrequency,
        point == null ? null : point.lastCheckedAt,
        overdueDays,
        blockedCode == null,
        blockedCode,
        blockedReasonOf(blockedCode),
        openOrderId);
  }

  /** 漏损级别权重：BURST 最大，TRACE 最小。 */
  public int leakRank() { return rankOf(LeakLevel.class, leakLevel); }

  /** 管段风险权重：EXTREME 最大，LOW 最小。 */
  public int riskRank() { return rankOf(RiskLevel.class, riskLevel); }

  /** 巡检超期天数 = 今天 - (上次巡检 + 周期天数)，负数表示未到期。 */
  public static long computeOverdueDays(InspectionPoint point) {
    if (point == null || point.lastCheckedAt == null) return 0L;
    LocalDate due = point.lastCheckedAt.toLocalDate().plusDays(CheckFrequency.daysOf(point.checkFrequency));
    return ChronoUnit.DAYS.between(due, LocalDate.now());
  }

  /** 派工阻塞判定：已驳回 > BURST 未核实 > 已有未关闭维修单。 */
  public static String blockedCodeOf(LeakReport report, Long openOrderId) {
    if (VerifyStatus.REJECTED.name().equals(report.verifyStatus)) return ErrorCodes.VERIFY_REJECTED;
    if (LeakLevel.BURST.name().equals(report.leakLevel) && !VerifyStatus.VERIFIED.name().equals(report.verifyStatus)) {
      return ErrorCodes.BURST_UNVERIFIED;
    }
    if (openOrderId != null) return ErrorCodes.ORDER_ALREADY_OPEN;
    return null;
  }

  public static String blockedReasonOf(String blockedCode) {
    if (blockedCode == null) return null;
    return switch (blockedCode) {
      case ErrorCodes.VERIFY_REJECTED -> ErrorMessages.VERIFY_REJECTED;
      case ErrorCodes.BURST_UNVERIFIED -> ErrorMessages.BURST_UNVERIFIED;
      case ErrorCodes.ORDER_ALREADY_OPEN -> ErrorMessages.ORDER_ALREADY_OPEN;
      default -> blockedCode;
    };
  }

  private static <E extends Enum<E>> int rankOf(Class<E> type, String value) {
    if (value == null) return -1;
    try { return Enum.valueOf(type, value.trim().toUpperCase()).ordinal(); }
    catch (IllegalArgumentException ex) { return -1; }
  }
}
