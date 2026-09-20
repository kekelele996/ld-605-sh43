export interface AuditLog {
  id: number;
  actor: string | null;
  action: string;
  targetType: string | null;
  targetId: number | null;
  detail: string | null;
  createdAt: string | null;
}
