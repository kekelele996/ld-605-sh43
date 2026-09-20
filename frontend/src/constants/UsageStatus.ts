export const UsageStatus = ["ISSUED","CONSUMED","RETURNED"] as const;
export type UsageStatus = (typeof UsageStatus)[number];
export const UsageStatusText: Record<UsageStatus, string> = {
  ISSUED: "已领用",
  CONSUMED: "已消耗",
  RETURNED: "已退回"
};
