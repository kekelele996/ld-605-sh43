package com.generated.waterLeak.types;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 领料 + 验收请求体。两者写在同一事务：
 * acceptancePassed=false 或库存不足时整体回滚，不得扣料或关单。
 */
public record RepairOrderAcceptPayload(
  @NotNull Boolean acceptancePassed,
  BigDecimal costAmount,
  String note,
  @Valid List<MaterialUsagePayload> items
) {}
