import { mockData } from "../mocks/seedData";
import type { LeakReportView } from "../types/LeakReportView";
import { post, request } from "./http";

const endpoint = "/api/leak-report";

export interface LeakReportCreatePayload {
  reporterType: string;
  pointId: number;
  leakLevel: string;
  description?: string;
  photoUrl?: string;
}

export async function listLeakReport(): Promise<LeakReportView[]> {
  try {
    return await request<LeakReportView[]>(endpoint);
  } catch {
    // 本地 mock 兜底，保证离线评审时页面可用。
    return [...(mockData.leakReport as unknown as LeakReportView[])];
  }
}

export function createLeakReport(payload: LeakReportCreatePayload) {
  return post<{ id: number; verify_status: string }>(endpoint, payload);
}

export function verifyLeakReport(id: number, result: "VERIFIED" | "REJECTED") {
  return post<{ id: number; verify_status: string }>(`${endpoint}/${id}/verify`, { result });
}
