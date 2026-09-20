export interface LeakReport {
  id: number;
  reporter_type: string;
  point_id: number;
  leak_level: string;
  description: string;
  reported_at: string;
  verify_status: string;
  photo_url: string;
}
