export const LeakLevel = ["TRACE","MINOR","MAJOR","BURST"] as const;
export type LeakLevel = (typeof LeakLevel)[number];
export const LeakLevelText: Record<LeakLevel, string> = Object.fromEntries(LeakLevel.map((value) => [value, value.replace(/_/g, " ")])) as Record<LeakLevel, string>;
