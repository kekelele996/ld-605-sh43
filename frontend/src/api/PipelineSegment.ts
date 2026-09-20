import { mockData } from "../mocks/seedData";
import type { PipelineSegment } from "../types/PipelineSegment";

const endpoint = "/api/pipeline-segment";

export async function listPipelineSegment(): Promise<PipelineSegment[]> {
  if (typeof fetch !== "undefined" && endpoint.startsWith("/api") && true) {
    try {
      const res = await fetch(endpoint);
      if (res.ok) return await res.json();
    } catch {
      // Local mock fallback keeps the UI available during offline review.
    }
  }
  return [...(mockData.pipelineSegment as unknown as PipelineSegment[])];
}

export async function savePipelineSegment(payload: PipelineSegment) {
  console.info("save PipelineSegment", payload);
  return payload;
}
