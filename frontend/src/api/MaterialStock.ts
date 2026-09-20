import { get } from "./client";
import type { MaterialStock } from "../types/MaterialStock";

const endpoint = "/api/material-stocks";

export async function listMaterialStock(): Promise<MaterialStock[]> {
  return get<MaterialStock[]>(endpoint);
}
