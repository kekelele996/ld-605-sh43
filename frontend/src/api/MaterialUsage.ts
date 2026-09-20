import { mockData } from "../mocks/seedData";
import type { MaterialUsage } from "../types/MaterialUsage";

const endpoint = "/api/material-usage";

export async function listMaterialUsage(): Promise<MaterialUsage[]> {
  if (typeof fetch !== "undefined" && endpoint.startsWith("/api") && true) {
    try {
      const res = await fetch(endpoint);
      if (res.ok) return await res.json();
    } catch {
      // Local mock fallback keeps the UI available during offline review.
    }
  }
  return [...(mockData.materialUsage as unknown as MaterialUsage[])];
}

export async function saveMaterialUsage(payload: MaterialUsage) {
  console.info("save MaterialUsage", payload);
  return payload;
}
