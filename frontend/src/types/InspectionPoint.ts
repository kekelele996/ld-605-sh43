export interface InspectionPoint {
  id: number;
  pipeline_segment_id: number;
  point_code: string;
  point_type: string;
  address_desc: string;
  check_frequency: string;
  last_checked_at: string;
  status: string;
}
