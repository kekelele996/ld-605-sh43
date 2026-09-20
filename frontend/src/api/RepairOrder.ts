import { get, post } from "./client";
import { mockData } from "../mocks/seedData";
import type { RepairOrder } from "../types/RepairOrder";
import type { AcceptInput, DispatchInput } from "../types/RepairFlow";

const endpoint = "/api/repair-orders";

export async function listRepairOrder(): Promise<RepairOrder[]> {
  try {
    return await get<RepairOrder[]>(endpoint);
  } catch {
    // Local mock fallback keeps the UI available during offline review.
    return [...(mockData.repairOrder as unknown as RepairOrder[])];
  }
}

/** 派工：BURST 未核实 / 同队同日重复派工会返回 409 业务错误。 */
export async function dispatchOrder(input: DispatchInput): Promise<RepairOrder> {
  return post<RepairOrder>(`${endpoint}/dispatch`, input);
}

/** 领料 + 验收：同一事务，验收失败不扣料、不关单。 */
export async function acceptOrder(id: number, input: AcceptInput): Promise<RepairOrder> {
  return post<RepairOrder>(`${endpoint}/${id}/accept`, input);
}
