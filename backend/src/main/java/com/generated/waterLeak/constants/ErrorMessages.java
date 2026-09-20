package com.generated.waterLeak.constants;

public final class ErrorMessages {
  public static final String AUTH_REQUIRED = "missing token";
  public static final String RBAC_DENIED = "role denied";
  public static final String VALIDATION_FAILED = "表单字段缺失或格式错误";
  public static final String REPORT_NOT_FOUND = "漏损报告不存在";
  public static final String POINT_NOT_FOUND = "巡检点不存在";
  public static final String ORDER_NOT_FOUND = "维修单不存在";
  public static final String BURST_UNVERIFIED = "BURST 级漏损未核实，不能派工";
  public static final String VERIFY_REJECTED = "该报告已被驳回，不能派工";
  public static final String ORDER_ALREADY_OPEN = "该漏损报告已存在未关闭维修单";
  public static final String CREW_DAY_CONFLICT = "同一维修队同一天只能有一张未关闭维修单";
  public static final String INSUFFICIENT_STOCK = "库存不足，无法领料";
  public static final String ACCEPTANCE_FAILED = "验收未通过：已回滚领料，维修单保持未关闭";
  public static final String ORDER_STATE_INVALID = "维修单已关闭，不允许重复领料或验收";
  public static final String INTERNAL_ERROR = "服务内部错误";
}
