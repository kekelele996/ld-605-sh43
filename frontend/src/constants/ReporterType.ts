export const ReporterType = ["RESIDENT","INSPECTOR"] as const;
export type ReporterType = (typeof ReporterType)[number];
export const ReporterTypeText: Record<ReporterType, string> = {
  RESIDENT: "居民上报",
  INSPECTOR: "巡检员上报"
};
