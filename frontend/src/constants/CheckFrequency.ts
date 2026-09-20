export const CheckFrequency = ["DAILY", "WEEKLY", "MONTHLY", "QUARTERLY"] as const;
export type CheckFrequency = (typeof CheckFrequency)[number];
export const CheckFrequencyDays: Record<CheckFrequency, number> = {
  DAILY: 1,
  WEEKLY: 7,
  MONTHLY: 30,
  QUARTERLY: 90
};
