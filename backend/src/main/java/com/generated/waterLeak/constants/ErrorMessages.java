package com.generated.waterLeak.constants;

public final class ErrorMessages {
  public static final String AUTH_REQUIRED = "missing token";
  public static final String RBAC_DENIED = "role denied";
  public static final String VALIDATION_FAILED = "表单字段缺失或格式错误";
  public static final String NOT_FOUND = "目标记录不存在";
  public static final String BURST_UNVERIFIED = "BURST 级漏损未核实，不能派工";
  public static final String CREW_DAY_CONFLICT = "该维修队当天已存在未关闭维修单，同一维修队同一天只能派工一单";
  public static final String ACCEPTANCE_FAILED = "验收未通过：本次领料与验收已整体回滚，未扣料、未关单";
  public static final String STOCK_INSUFFICIENT = "仓库库存不足，领料失败";
  public static final String ORDER_STATE_INVALID = "维修单当前状态不允许该操作";

  private ErrorMessages() {}
}
