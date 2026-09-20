import { get } from "./client";
import { mockData } from "../mocks/seedData";
import type { InspectionPoint } from "../types/InspectionPoint";

const endpoint = "/api/inspection-points";

export async function listInspectionPoint(): Promise<InspectionPoint[]> {
  try {
    return await get<InspectionPoint[]>(endpoint);
  } catch {
    // Local mock fallback keeps the UI available during offline review.
    return [...(mockData.inspectionPoint as unknown as InspectionPoint[])];
  }
}
