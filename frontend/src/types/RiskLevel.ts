export const RiskLevel = ["LOW","MEDIUM","HIGH","EXTREME"] as const;
export type RiskLevel = (typeof RiskLevel)[number];
export const RiskLevelText: Record<RiskLevel, string> = Object.fromEntries(RiskLevel.map((value) => [value, value.replace(/_/g, " ")])) as Record<RiskLevel, string>;
