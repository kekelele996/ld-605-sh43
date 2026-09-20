import { get } from "./client";
import { mockData } from "../mocks/seedData";
import type { PipelineSegment } from "../types/PipelineSegment";

const endpoint = "/api/pipeline-segments";

export async function listPipelineSegment(): Promise<PipelineSegment[]> {
  try {
    return await get<PipelineSegment[]>(endpoint);
  } catch {
    // Local mock fallback keeps the UI available during offline review.
    return [...(mockData.pipelineSegment as unknown as PipelineSegment[])];
  }
}
