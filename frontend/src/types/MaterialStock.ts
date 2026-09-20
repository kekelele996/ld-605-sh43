export interface MaterialStock {
  id: number;
  materialCode: string;
  materialName: string;
  unit: string | null;
  warehouse: string;
  quantity: number;
}
