import { useMemo } from "react";
import type { LeakReportView } from "../types/LeakReportView";

/** 漏损严重度统计：按等级分桶 + 待核实数量 + 超期巡检数量，供态势卡片使用。 */
export function useLeakSeverity(rows: LeakReportView[] = []) {
  return useMemo(() => {
    const byLevel = { BURST: 0, MAJOR: 0, MINOR: 0, TRACE: 0 } as Record<string, number>;
    let pendingVerify = 0;
    let overdue = 0;
    for (const row of rows) {
      if (row.leak_level in byLevel) byLevel[row.leak_level] += 1;
      if (row.verify_status === "PENDING") pendingVerify += 1;
      if (row.overdue_days > 0) overdue += 1;
    }
    return { byLevel, pendingVerify, overdue, total: rows.length };
  }, [rows]);
}
