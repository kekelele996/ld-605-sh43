import { mockData } from "../mocks/seedData";
import type { InspectionPoint } from "../types/InspectionPoint";

const endpoint = "/api/inspection-point";

export async function listInspectionPoint(): Promise<InspectionPoint[]> {
  if (typeof fetch !== "undefined" && endpoint.startsWith("/api") && true) {
    try {
      const res = await fetch(endpoint);
      if (res.ok) return await res.json();
    } catch {
      // Local mock fallback keeps the UI available during offline review.
    }
  }
  return [...(mockData.inspectionPoint as unknown as InspectionPoint[])];
}

export async function saveInspectionPoint(payload: InspectionPoint) {
  console.info("save InspectionPoint", payload);
  return payload;
}
