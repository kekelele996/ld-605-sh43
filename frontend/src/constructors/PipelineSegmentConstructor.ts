import type { PipelineSegment } from "../types/PipelineSegment";

export const createDefaultPipelineSegment = (overrides: Partial<PipelineSegment> = {}): PipelineSegment => ({
  id: 0,
  segment_code: "",
  district: "",
  material: "",
  diameter: "",
  install_year: "",
  pressure_zone: "",
  risk_level: "LOW",
  ...overrides
});

export const createPipelineSegmentForm = createDefaultPipelineSegment;
export const createPipelineSegmentResponse = createDefaultPipelineSegment;
