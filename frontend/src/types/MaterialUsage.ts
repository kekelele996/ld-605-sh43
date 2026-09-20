export interface MaterialUsage {
  id: number;
  repair_order_id: number;
  material_code: string;
  material_name: string;
  quantity: number;
  unit: string;
  warehouse: string;
  usage_status: string;
}
