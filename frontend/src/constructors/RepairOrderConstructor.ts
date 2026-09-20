import type { RepairOrder } from "../types/RepairOrder";

export const createDefaultRepairOrder = (overrides: Partial<RepairOrder> = {}): RepairOrder => ({
  id: 1 as never,
  leak_report_id: 1 as never,
  crew_id: 1 as never,
  priority: "priority 1" as never,
  status: "ASSIGNED" as never,
  planned_start: "planned start 1" as never,
  finished_at: "2026-06-11T09:00:00Z" as never,
  cost_amount: 15400 as never,
  ...overrides
});

export const createRepairOrderForm = createDefaultRepairOrder;
export const createRepairOrderResponse = createDefaultRepairOrder;
