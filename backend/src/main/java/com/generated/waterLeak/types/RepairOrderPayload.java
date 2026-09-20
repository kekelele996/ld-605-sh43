package com.generated.waterLeak.types;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/** 维修派工请求体。dispatchDate 缺省为当天，用于“同一维修队同一天一单”判定。 */
public record RepairOrderPayload(
  @NotNull Long leakReportId,
  @NotNull Long crewId,
  String priority,
  LocalDate dispatchDate
) {}
