package com.generated.waterLeak.constructors;

import com.generated.waterLeak.models.LeakReport;
import java.util.LinkedHashMap;
import java.util.Map;

public final class LeakReportDtoFactory {

  public static Map<String, Object> toResponse(LeakReport report) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("id", report.id);
    body.put("reporter_type", report.reporterType);
    body.put("point_id", report.pointId);
    body.put("leak_level", report.leakLevel);
    body.put("description", report.description);
    body.put("reported_at", report.reportedAt == null ? null : report.reportedAt.toString());
    body.put("verify_status", report.verifyStatus);
    body.put("photo_url", report.photoUrl);
    return body;
  }

  public static Map<String, Object> toQueueResponse(VerificationQueueItem item) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("report_id", item.reportId());
    body.put("point_id", item.pointId());
    body.put("point_code", item.pointCode());
    body.put("segment_id", item.segmentId());
    body.put("segment_code", item.segmentCode());
    body.put("district", item.district());
    body.put("leak_level", item.leakLevel());
    body.put("verify_status", item.verifyStatus());
    body.put("description", item.description());
    body.put("reported_at", item.reportedAt() == null ? null : item.reportedAt().toString());
    body.put("risk_level", item.riskLevel());
    body.put("check_frequency", item.checkFrequency());
    body.put("last_checked_at", item.lastCheckedAt() == null ? null : item.lastCheckedAt().toString());
    body.put("overdue_days", item.overdueDays());
    body.put("dispatchable", item.dispatchable());
    body.put("blocked_code", item.blockedCode());
    body.put("blocked_reason", item.blockedReason());
    body.put("open_order_id", item.openOrderId());
    return body;
  }
}
