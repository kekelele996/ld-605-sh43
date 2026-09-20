package com.generated.waterLeak.constructors;

import com.generated.waterLeak.models.PipelineSegment;
import java.util.LinkedHashMap;
import java.util.Map;

/** 管网分段响应构造器。 */
public final class PipelineSegmentDtoFactory {

  private PipelineSegmentDtoFactory() {}

  public static Map<String, Object> toResponse(PipelineSegment segment) {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("id", segment.getId());
    map.put("segment_code", segment.getSegmentCode());
    map.put("district", segment.getDistrict());
    map.put("material", segment.getMaterial());
    map.put("diameter", segment.getDiameter());
    map.put("install_year", segment.getInstallYear());
    map.put("pressure_zone", segment.getPressureZone());
    map.put("risk_level", segment.getRiskLevel());
    return map;
  }
}
