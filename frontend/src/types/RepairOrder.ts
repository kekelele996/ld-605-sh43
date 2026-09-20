export interface RepairOrder {
  id: number;
  leak_report_id: number;
  crew_id: number;
  priority: string;
  status: string;
  planned_start: string | null;
  finished_at: string | null;
  cost_amount: number | null;
  dispatch_date: string | null;
  block_reason: string | null;
  created_at: string | null;
}
