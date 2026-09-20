export const RepairStatus = ["WAIT_ASSIGN","ASSIGNED","WORKING","ACCEPTANCE","CLOSED"] as const;
export type RepairStatus = (typeof RepairStatus)[number];
export const RepairStatusText: Record<RepairStatus, string> = Object.fromEntries(RepairStatus.map((value) => [value, value.replace(/_/g, " ")])) as Record<RepairStatus, string>;
