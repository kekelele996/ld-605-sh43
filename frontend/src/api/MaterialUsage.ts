import { mockData } from "../mocks/seedData";
import type { MaterialStock } from "../types/MaterialStock";
import type { MaterialUsage } from "../types/MaterialUsage";
import { request } from "./http";

const endpoint = "/api/material-usage";

export async function listMaterialUsage(repairOrderId?: number): Promise<MaterialUsage[]> {
  try {
    const query = repairOrderId == null ? "" : `?repairOrderId=${repairOrderId}`;
    return await request<MaterialUsage[]>(endpoint + query);
  } catch {
    return [...(mockData.materialUsage as unknown as MaterialUsage[])];
  }
}

export async function listMaterialStock(): Promise<MaterialStock[]> {
  try {
    return await request<MaterialStock[]>(`${endpoint}/stock`);
  } catch {
    return [...(mockData.materialStock as unknown as MaterialStock[])];
  }
}
