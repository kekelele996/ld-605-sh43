/** 漏损核查列表行：后端联查巡检点 + 管段后的视图结构。 */
export interface LeakReportView {
  id: number;
  reporter_type: string;
  point_id: number;
  leak_level: string;
  description: string;
  reported_at: string;
  verify_status: string;
  photo_url: string;
  block_reason: string | null;
  point_code: string;
  check_frequency: string;
  last_checked_at: string | null;
  segment_id: number;
  segment_code: string;
  district: string;
  risk_level: string;
  overdue_days: number;
}
