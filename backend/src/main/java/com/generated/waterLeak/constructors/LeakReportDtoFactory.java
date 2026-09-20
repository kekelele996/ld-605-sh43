package com.generated.waterLeak.constructors;

import com.generated.waterLeak.types.LeakReportView;
import java.util.LinkedHashMap;
import java.util.Map;

/** 漏损报告响应构造器：页面/store 不得直接散写默认结构。 */
public final class LeakReportDtoFactory {

  private LeakReportDtoFactory() {}

  public static Map<String, Object> toResponse(LeakReportView view, long overdueDays, String blockReason) {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("id", view.id());
    map.put("reporter_type", view.reporterType());
    map.put("point_id", view.pointId());
    map.put("leak_level", view.leakLevel());
    map.put("description", view.description());
    map.put("reported_at", view.reportedAt());
    map.put("verify_status", view.verifyStatus());
    map.put("photo_url", view.photoUrl());
    map.put("block_reason", blockReason);
    map.put("point_code", view.pointCode());
    map.put("check_frequency", view.checkFrequency());
    map.put("last_checked_at", view.lastCheckedAt());
    map.put("segment_id", view.segmentId());
    map.put("segment_code", view.segmentCode());
    map.put("district", view.district());
    map.put("risk_level", view.riskLevel());
    map.put("overdue_days", overdueDays);
    return map;
  }
}
