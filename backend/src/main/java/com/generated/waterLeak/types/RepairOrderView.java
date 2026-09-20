package com.generated.waterLeak.types;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** 维修单列表视图行：维修单 + 维修队 + 关联漏损等级的联查结果。 */
public record RepairOrderView(
  Long id,
  Long leakReportId,
  Long crewId,
  String crewCode,
  String crewName,
  String leakLevel,
  String priority,
  String status,
  LocalDateTime plannedStart,
  LocalDateTime finishedAt,
  BigDecimal costAmount,
  LocalDate dispatchDate,
  String blockReason,
  LocalDateTime createdAt
) {}
