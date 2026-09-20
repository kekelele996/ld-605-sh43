import { mockData } from "../mocks/seedData";
import type { InspectionPointView } from "../types/InspectionPointView";
import { request } from "./http";

const endpoint = "/api/inspection-point";

export async function listInspectionPoint(): Promise<InspectionPointView[]> {
  try {
    return await request<InspectionPointView[]>(endpoint);
  } catch {
    return [...(mockData.inspectionPoint as unknown as InspectionPointView[])];
  }
}
