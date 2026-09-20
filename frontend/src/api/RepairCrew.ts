import { get } from "./client";
import type { RepairCrew } from "../types/RepairCrew";

const endpoint = "/api/crews";

export async function listRepairCrew(): Promise<RepairCrew[]> {
  return get<RepairCrew[]>(endpoint);
}
