import type { LeakReport } from "../types/LeakReport";

export const createDefaultLeakReport = (overrides: Partial<LeakReport> = {}): LeakReport => ({
  id: 1 as never,
  reporter_type: "MINOR" as never,
  point_id: 1 as never,
  leak_level: "LOW" as never,
  description: "description 1" as never,
  reported_at: "2026-06-11T09:00:00Z" as never,
  verify_status: "ASSIGNED" as never,
  photo_url: "/mock/photo_url-1.png" as never,
  ...overrides
});

export const createLeakReportForm = createDefaultLeakReport;
export const createLeakReportResponse = createDefaultLeakReport;
