/** 维修单列表行：后端联查维修队 + 漏损报告后的视图结构。 */
export interface RepairOrderView {
  id: number;
  leak_report_id: number;
  crew_id: number;
  crew_code: string;
  crew_name: string;
  leak_level: string | null;
  priority: string;
  status: string;
  planned_start: string | null;
  finished_at: string | null;
  cost_amount: number | null;
  dispatch_date: string | null;
  block_reason: string | null;
  created_at: string | null;
}
