import type { MaterialUsage } from "../types/MaterialUsage";

export const createDefaultMaterialUsage = (overrides: Partial<MaterialUsage> = {}): MaterialUsage => ({
  id: 1 as never,
  repair_order_id: 1 as never,
  material_code: "material code 1" as never,
  material_name: "material name 1" as never,
  quantity: 92 as never,
  unit: "unit 1" as never,
  warehouse: "warehouse 1" as never,
  usage_status: "ASSIGNED" as never,
  ...overrides
});

export const createMaterialUsageForm = createDefaultMaterialUsage;
export const createMaterialUsageResponse = createDefaultMaterialUsage;
