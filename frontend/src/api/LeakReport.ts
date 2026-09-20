import { get, post } from "./client";
import { mockData } from "../mocks/seedData";
import type { LeakReport } from "../types/LeakReport";
import type { VerificationQueueItem } from "../types/VerificationQueueItem";
import type { ReportInput } from "../types/RepairFlow";

const endpoint = "/api/leak-reports";

export async function listLeakReport(): Promise<LeakReport[]> {
  try {
    return await get<LeakReport[]>(endpoint);
  } catch {
    // Local mock fallback keeps the UI available during offline review.
    return [...(mockData.leakReport as unknown as LeakReport[])];
  }
}

/** 漏损核查队列：服务端按 BURST→TRACE、管段风险高→低、同分看巡检超期排序。 */
export async function fetchVerificationQueue(): Promise<VerificationQueueItem[]> {
  return get<VerificationQueueItem[]>(`${endpoint}/verification-queue`);
}

/** 漏损上报。 */
export async function reportLeak(input: ReportInput): Promise<LeakReport> {
  return post<LeakReport>(endpoint, input);
}

/** 漏损核实：VERIFIED / REJECTED。 */
export async function verifyLeak(id: number, verifyStatus: string): Promise<LeakReport> {
  return post<LeakReport>(`${endpoint}/${id}/verify`, { verifyStatus });
}
