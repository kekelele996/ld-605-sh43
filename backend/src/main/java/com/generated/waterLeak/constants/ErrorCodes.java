package com.generated.waterLeak.constants;

public final class ErrorCodes {
  public static final String AUTH_REQUIRED = "AUTH_REQUIRED";
  public static final String RBAC_DENIED = "RBAC_DENIED";
  public static final String VALIDATION_FAILED = "VALIDATION_FAILED";
  public static final String NOT_FOUND = "NOT_FOUND";
  /** BURST 级漏损未核实，禁止派工。 */
  public static final String BURST_UNVERIFIED = "BURST_UNVERIFIED";
  /** 同一维修队同一天只能有一张未关闭维修单。 */
  public static final String CREW_DAY_CONFLICT = "CREW_DAY_CONFLICT";
  /** 验收未通过：领料与验收在同一事务内回滚，不得扣料或关单。 */
  public static final String ACCEPTANCE_FAILED = "ACCEPTANCE_FAILED";
  /** 仓库库存不足，领料失败。 */
  public static final String STOCK_INSUFFICIENT = "STOCK_INSUFFICIENT";
  /** 维修单当前状态不允许该操作。 */
  public static final String ORDER_STATE_INVALID = "ORDER_STATE_INVALID";

  private ErrorCodes() {}
}
