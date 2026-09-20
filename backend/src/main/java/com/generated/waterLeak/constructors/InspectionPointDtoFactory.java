package com.generated.waterLeak.constructors;

import com.generated.waterLeak.types.InspectionPointView;
import java.util.LinkedHashMap;
import java.util.Map;

/** 巡检点响应构造器。 */
public final class InspectionPointDtoFactory {

  private InspectionPointDtoFactory() {}

  public static Map<String, Object> toResponse(InspectionPointView view, long overdueDays) {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("id", view.id());
    map.put("pipeline_segment_id", view.pipelineSegmentId());
    map.put("point_code", view.pointCode());
    map.put("point_type", view.pointType());
    map.put("address_desc", view.addressDesc());
    map.put("check_frequency", view.checkFrequency());
    map.put("last_checked_at", view.lastCheckedAt());
    map.put("status", view.status());
    map.put("segment_code", view.segmentCode());
    map.put("risk_level", view.riskLevel());
    map.put("overdue_days", overdueDays);
    return map;
  }
}
