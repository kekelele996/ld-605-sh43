import type { RepairOrder } from "../types/RepairOrder";

export const createDefaultRepairOrder = (overrides: Partial<RepairOrder> = {}): RepairOrder => ({
  id: 0,
  leak_report_id: 0,
  crew_id: 0,
  crew_name: null,
  leak_level: null,
  priority: "MAJOR",
  status: "ASSIGNED",
  planned_start: null,
  work_date: null,
  finished_at: null,
  cost_amount: null,
  ...overrides
});

export const createRepairOrderForm = createDefaultRepairOrder;
export const createRepairOrderResponse = createDefaultRepairOrder;
