package com.generated.waterLeak.types;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** 漏损上报请求体。 */
public record LeakReportPayload(
  @NotBlank String reporterType,
  @NotNull Long pointId,
  @NotBlank String leakLevel,
  String description,
  String photoUrl
) {}
