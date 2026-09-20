export const VerifyStatus = ["PENDING", "VERIFIED", "REJECTED"] as const;
export type VerifyStatus = (typeof VerifyStatus)[number];
export const VerifyStatusText: Record<VerifyStatus, string> = {
  PENDING: "待核实",
  VERIFIED: "已核实",
  REJECTED: "已驳回"
};
