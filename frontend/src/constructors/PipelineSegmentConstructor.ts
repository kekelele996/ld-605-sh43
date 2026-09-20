import type { PipelineSegment } from "../types/PipelineSegment";

export const createDefaultPipelineSegment = (overrides: Partial<PipelineSegment> = {}): PipelineSegment => ({
  id: 1 as never,
  segment_code: "segment code 1" as never,
  district: "district 1" as never,
  material: "material 1" as never,
  diameter: "diameter 1" as never,
  install_year: "install year 1" as never,
  pressure_zone: "pressure zone 1" as never,
  risk_level: "LOW" as never,
  ...overrides
});

export const createPipelineSegmentForm = createDefaultPipelineSegment;
export const createPipelineSegmentResponse = createDefaultPipelineSegment;
