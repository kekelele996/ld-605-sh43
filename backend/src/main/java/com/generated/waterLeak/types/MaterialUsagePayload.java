package com.generated.waterLeak.types;

import java.math.BigDecimal;
import java.util.List;

/** 领料 + 验收请求体：两者在同一事务内提交或整体回滚。 */
public record MaterialUsagePayload(List<MaterialLine> materials, Boolean acceptancePassed, BigDecimal costAmount, String note) {
  public record MaterialLine(String materialCode, String warehouse, Integer quantity) {}
}
