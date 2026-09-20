import { mockData } from "../mocks/seedData";
import type { RepairCrew } from "../types/RepairCrew";
import type { RepairOrderView } from "../types/RepairOrderView";
import { post, request } from "./http";

const endpoint = "/api/repair-order";

export interface DispatchPayload {
  leakReportId: number;
  crewId: number;
  priority?: string;
  dispatchDate?: string;
}

export interface AcceptItemPayload {
  materialCode: string;
  materialName: string;
  quantity: number;
  unit: string;
  warehouse: string;
}

export interface AcceptPayload {
  acceptancePassed: boolean;
  costAmount?: number;
  note?: string;
  items: AcceptItemPayload[];
}

export async function listRepairOrder(): Promise<RepairOrderView[]> {
  try {
    return await request<RepairOrderView[]>(endpoint);
  } catch {
    return [...(mockData.repairOrder as unknown as RepairOrderView[])];
  }
}

export async function listRepairCrew(): Promise<RepairCrew[]> {
  try {
    return await request<RepairCrew[]>(`${endpoint}/crews`);
  } catch {
    return [...(mockData.repairCrew as unknown as RepairCrew[])];
  }
}

export function dispatchRepairOrder(payload: DispatchPayload) {
  return post<{ id: number; status: string; dispatch_date: string }>(`${endpoint}/dispatch`, payload);
}

export function acceptRepairOrder(id: number, payload: AcceptPayload) {
  return post<{ id: number; status: string }>(`${endpoint}/${id}/accept`, payload);
}
