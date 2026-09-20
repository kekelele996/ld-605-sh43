import { get } from "./client";
import type { AuditLog } from "../types/AuditLog";

const endpoint = "/api/audit-logs";

export async function listAuditLog(): Promise<AuditLog[]> {
  return get<AuditLog[]>(endpoint);
}
