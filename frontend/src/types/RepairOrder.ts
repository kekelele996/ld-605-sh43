export interface RepairOrder {
  id: number;
  leak_report_id: number;
  crew_id: number;
  crew_name: string | null;
  leak_level: string | null;
  priority: string;
  status: string;
  planned_start: string | null;
  work_date: string | null;
  finished_at: string | null;
  cost_amount: number | null;
}
