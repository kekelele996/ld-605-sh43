import { mockData } from "../mocks/seedData";
import type { PipelineSegment } from "../types/PipelineSegment";
import { request } from "./http";

const endpoint = "/api/pipeline-segment";

export async function listPipelineSegment(): Promise<PipelineSegment[]> {
  try {
    return await request<PipelineSegment[]>(endpoint);
  } catch {
    return [...(mockData.pipelineSegment as unknown as PipelineSegment[])];
  }
}
