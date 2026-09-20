import { mockData } from "../mocks/seedData";
import type { RepairOrder } from "../types/RepairOrder";

const endpoint = "/api/repair-order";

export async function listRepairOrder(): Promise<RepairOrder[]> {
  if (typeof fetch !== "undefined" && endpoint.startsWith("/api") && true) {
    try {
      const res = await fetch(endpoint);
      if (res.ok) return await res.json();
    } catch {
      // Local mock fallback keeps the UI available during offline review.
    }
  }
  return [...(mockData.repairOrder as unknown as RepairOrder[])];
}

export async function saveRepairOrder(payload: RepairOrder) {
  console.info("save RepairOrder", payload);
  return payload;
}
