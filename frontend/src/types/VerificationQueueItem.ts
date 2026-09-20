/** 漏损核查队列行：服务端按 漏损级别→管段风险→巡检超期 排序后返回，含派工阻塞原因。 */
export interface VerificationQueueItem {
  report_id: number;
  point_id: number;
  point_code: string | null;
  segment_id: number | null;
  segment_code: string | null;
  district: string | null;
  leak_level: string;
  verify_status: string;
  description: string | null;
  reported_at: string | null;
  risk_level: string | null;
  check_frequency: string | null;
  last_checked_at: string | null;
  overdue_days: number;
  dispatchable: boolean;
  blocked_code: string | null;
  blocked_reason: string | null;
  open_order_id: number | null;
}
