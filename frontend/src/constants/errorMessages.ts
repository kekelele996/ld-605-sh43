export const ERROR_MESSAGES: Record<string, string> = {
  AUTH_REQUIRED: "请先登录后再继续操作",
  RBAC_DENIED: "当前角色没有执行该动作的权限",
  VALIDATION_FAILED: "表单字段缺失或格式错误",
  RATE_LIMITED: "请求过于频繁，请稍后再试",
  REPORT_NOT_FOUND: "漏损报告不存在",
  POINT_NOT_FOUND: "巡检点不存在",
  ORDER_NOT_FOUND: "维修单不存在",
  BURST_UNVERIFIED: "BURST 级漏损未核实，不能派工",
  VERIFY_REJECTED: "该报告已被驳回，不能派工",
  ORDER_ALREADY_OPEN: "该漏损报告已存在未关闭维修单",
  CREW_DAY_CONFLICT: "同一维修队同一天只能有一张未关闭维修单",
  INSUFFICIENT_STOCK: "库存不足，无法领料",
  ACCEPTANCE_FAILED: "验收未通过：已回滚领料，维修单保持未关闭",
  ORDER_STATE_INVALID: "维修单已关闭，不允许重复领料或验收",
  INTERNAL_ERROR: "服务内部错误，请稍后再试"
};
