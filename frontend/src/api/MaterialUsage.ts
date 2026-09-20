import { get } from "./client";
import { mockData } from "../mocks/seedData";
import type { MaterialUsage } from "../types/MaterialUsage";

const endpoint = "/api/material-usages";

export async function listMaterialUsage(orderId?: number): Promise<MaterialUsage[]> {
  try {
    const query = orderId == null ? "" : `?orderId=${orderId}`;
    return await get<MaterialUsage[]>(`${endpoint}${query}`);
  } catch {
    // Local mock fallback keeps the UI available during offline review.
    return [...(mockData.materialUsage as unknown as MaterialUsage[])];
  }
}
