import { mockData } from "../mocks/seedData";
import type { LeakReport } from "../types/LeakReport";

const endpoint = "/api/leak-report";

export async function listLeakReport(): Promise<LeakReport[]> {
  if (typeof fetch !== "undefined" && endpoint.startsWith("/api") && true) {
    try {
      const res = await fetch(endpoint);
      if (res.ok) return await res.json();
    } catch {
      // Local mock fallback keeps the UI available during offline review.
    }
  }
  return [...(mockData.leakReport as unknown as LeakReport[])];
}

export async function saveLeakReport(payload: LeakReport) {
  console.info("save LeakReport", payload);
  return payload;
}
