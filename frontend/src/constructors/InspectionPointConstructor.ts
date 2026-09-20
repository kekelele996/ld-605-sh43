import type { InspectionPoint } from "../types/InspectionPoint";

export const createDefaultInspectionPoint = (overrides: Partial<InspectionPoint> = {}): InspectionPoint => ({
  id: 0,
  pipeline_segment_id: 0,
  point_code: "",
  point_type: "阀门井",
  address_desc: "",
  check_frequency: "30d",
  last_checked_at: "",
  status: "ACTIVE",
  ...overrides
});

export const createInspectionPointForm = createDefaultInspectionPoint;
export const createInspectionPointResponse = createDefaultInspectionPoint;
