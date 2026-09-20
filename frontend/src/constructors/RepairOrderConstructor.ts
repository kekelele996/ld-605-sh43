import type { RepairOrder } from "../types/RepairOrder";

export const createDefaultRepairOrder = (overrides: Partial<RepairOrder> = {}): RepairOrder => ({
  id: 0,
  leak_report_id: 0,
  crew_id: 0,
  priority: "P2",
  status: "ASSIGNED",
  planned_start: null,
  finished_at: null,
  cost_amount: null,
  dispatch_date: null,
  block_reason: null,
  created_at: null,
  ...overrides
});

export const createRepairOrderForm = createDefaultRepairOrder;
export const createRepairOrderResponse = createDefaultRepairOrder;
