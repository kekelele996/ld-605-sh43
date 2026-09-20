import type { LeakReport } from "../types/LeakReport";

export const createDefaultLeakReport = (overrides: Partial<LeakReport> = {}): LeakReport => ({
  id: 0,
  reporter_type: "RESIDENT",
  point_id: 0,
  leak_level: "MINOR",
  description: "",
  reported_at: "",
  verify_status: "PENDING",
  photo_url: "",
  block_reason: null,
  ...overrides
});

export const createLeakReportForm = createDefaultLeakReport;
export const createLeakReportResponse = createDefaultLeakReport;
