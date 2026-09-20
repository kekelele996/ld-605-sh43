package com.generated.waterLeak.types;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/** 单条领料明细。 */
public record MaterialUsagePayload(
  @NotBlank String materialCode,
  @NotBlank String materialName,
  @NotNull @DecimalMin(value = "0", inclusive = false) BigDecimal quantity,
  @NotBlank String unit,
  @NotBlank String warehouse
) {}
