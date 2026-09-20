export const ERROR_MESSAGES: Record<string, string> = {
  AUTH_REQUIRED: "请先登录后再继续操作",
  RBAC_DENIED: "当前角色没有执行该动作的权限",
  VALIDATION_FAILED: "表单字段缺失或格式错误",
  RATE_LIMITED: "请求过于频繁，请稍后再试",
  NOT_FOUND: "目标记录不存在",
  BURST_UNVERIFIED: "BURST 级漏损未核实，不能派工",
  CREW_DAY_CONFLICT: "该维修队当天已存在未关闭维修单，同一维修队同一天只能派工一单",
  ACCEPTANCE_FAILED: "验收未通过：本次领料与验收已整体回滚，未扣料、未关单",
  STOCK_INSUFFICIENT: "仓库库存不足，领料失败",
  ORDER_STATE_INVALID: "维修单当前状态不允许该操作",
  UNKNOWN: "操作失败，请稍后重试"
};
