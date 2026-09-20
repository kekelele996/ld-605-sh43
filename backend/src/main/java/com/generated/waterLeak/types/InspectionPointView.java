package com.generated.waterLeak.types;

import java.time.LocalDateTime;

/** 巡检点列表视图行：巡检点 + 所属管段的联查结果。 */
public record InspectionPointView(
  Long id,
  Long pipelineSegmentId,
  String pointCode,
  String pointType,
  String addressDesc,
  String checkFrequency,
  LocalDateTime lastCheckedAt,
  String status,
  String segmentCode,
  String riskLevel
) {}
