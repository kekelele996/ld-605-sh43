package com.generated.waterLeak.types;

import java.time.LocalDateTime;

/**
 * 漏损核查列表视图行：漏损报告 + 巡检点 + 管段的联查结果。
 * 排序规则：漏损等级 BURST→TRACE，管段风险从高到低，同分看巡检超期（超期天数多者靠前）。
 */
public record LeakReportView(
  Long id,
  String reporterType,
  Long pointId,
  String leakLevel,
  String description,
  LocalDateTime reportedAt,
  String verifyStatus,
  String photoUrl,
  String blockReason,
  String pointCode,
  String checkFrequency,
  LocalDateTime lastCheckedAt,
  Long segmentId,
  String segmentCode,
  String district,
  String riskLevel
) {}
