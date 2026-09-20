/** 巡检点列表行：后端联查管段后的视图结构。 */
export interface InspectionPointView {
  id: number;
  pipeline_segment_id: number;
  point_code: string;
  point_type: string;
  address_desc: string;
  check_frequency: string;
  last_checked_at: string | null;
  status: string;
  segment_code: string;
  risk_level: string;
  overdue_days: number;
}
