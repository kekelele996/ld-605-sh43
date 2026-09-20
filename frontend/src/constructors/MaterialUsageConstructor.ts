import type { MaterialUsage } from "../types/MaterialUsage";

export const createDefaultMaterialUsage = (overrides: Partial<MaterialUsage> = {}): MaterialUsage => ({
  id: 0,
  repair_order_id: 0,
  material_code: "",
  material_name: "",
  quantity: 1,
  unit: "个",
  warehouse: "中心仓库",
  usage_status: "ISSUED",
  ...overrides
});

export const createMaterialUsageForm = createDefaultMaterialUsage;
export const createMaterialUsageResponse = createDefaultMaterialUsage;
