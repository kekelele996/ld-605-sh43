export interface RepairOrder {
  id: number;
  leak_report_id: number;
  crew_id: number;
  priority: string;
  status: string;
  planned_start: string;
  finished_at: string;
  cost_amount: number;
}
