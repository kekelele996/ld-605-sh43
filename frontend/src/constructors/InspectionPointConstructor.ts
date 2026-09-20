import type { InspectionPoint } from "../types/InspectionPoint";

export const createDefaultInspectionPoint = (overrides: Partial<InspectionPoint> = {}): InspectionPoint => ({
  id: 1 as never,
  pipeline_segment_id: 1 as never,
  point_code: "point code 1" as never,
  point_type: "MINOR" as never,
  address_desc: "address desc 1" as never,
  check_frequency: "check frequency 1" as never,
  last_checked_at: "2026-06-11T09:00:00Z" as never,
  status: "ASSIGNED" as never,
  ...overrides
});

export const createInspectionPointForm = createDefaultInspectionPoint;
export const createInspectionPointResponse = createDefaultInspectionPoint;
