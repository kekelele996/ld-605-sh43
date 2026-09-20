export const formatDate = (value: string | null | undefined) =>
  value ? new Date(value).toLocaleString("zh-CN") : "—";

export const formatStatus = (value: string) => value.replace(/_/g, " ");

export const formatNumber = (value: number | null | undefined) =>
  value == null ? "—" : new Intl.NumberFormat("zh-CN").format(value);

export const formatMoney = (value: number | null | undefined) =>
  value == null ? "—" : "¥" + new Intl.NumberFormat("zh-CN").format(value);

export const formatRisk = (value: string) =>
  ({ LOW: "低", MEDIUM: "中", HIGH: "高", CRITICAL: "严重", EXTREME: "极高" }[value] ?? value);

export const formatLeakLevel = (value: string) =>
  ({ TRACE: "痕迹", MINOR: "轻微", MAJOR: "严重", BURST: "爆管" }[value] ?? value);

export const formatVerifyStatus = (value: string) =>
  ({ PENDING: "待核实", VERIFIED: "已核实", REJECTED: "误报驳回" }[value] ?? value);

export const formatRepairStatus = (value: string) =>
  ({ WAIT_ASSIGN: "待派工", ASSIGNED: "已派工", WORKING: "维修中", ACCEPTANCE: "待验收", CLOSED: "已关闭" }[value] ?? value);

export const formatReporterType = (value: string) =>
  ({ RESIDENT: "居民上报", INSPECTOR: "巡检员上报" }[value] ?? value);

export const formatUsageStatus = (value: string) =>
  ({ ISSUED: "已领用", CONSUMED: "已消耗", RETURNED: "已退回" }[value] ?? value);

/** 巡检超期：>0 显示超期天数，否则显示未超期。 */
export const formatOverdue = (days: number) => (days > 0 ? `超期 ${days} 天` : "未超期");
