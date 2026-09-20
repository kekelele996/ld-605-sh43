/**
 * 本地种子数据：仅在后端不可达时兜底展示，正式数据以后端 API + 数据库为准。
 * 结构与 database/init.sql 种子保持一致。
 */
export const mockData = {
  pipelineSegment: [
    { id: 1, segment_code: "SEG-001", district: "城东片区", material: "铸铁", diameter: "DN300", install_year: "1998", pressure_zone: "高压区", risk_level: "EXTREME" },
    { id: 2, segment_code: "SEG-002", district: "城西片区", material: "PE", diameter: "DN200", install_year: "2010", pressure_zone: "中压区", risk_level: "HIGH" },
    { id: 3, segment_code: "SEG-003", district: "城南片区", material: "钢管", diameter: "DN150", install_year: "2015", pressure_zone: "中压区", risk_level: "MEDIUM" },
    { id: 4, segment_code: "SEG-004", district: "城北片区", material: "PE", diameter: "DN100", install_year: "2020", pressure_zone: "低压区", risk_level: "LOW" },
    { id: 5, segment_code: "SEG-005", district: "高新区", material: "球墨铸铁", diameter: "DN250", install_year: "2005", pressure_zone: "高压区", risk_level: "HIGH" }
  ],
  inspectionPoint: [
    { id: 1, pipeline_segment_id: 1, point_code: "PT-1001", point_type: "阀门井", address_desc: "城东路口西北角阀门井", check_frequency: "7d", last_checked_at: "2026-09-01T09:00:00", status: "ACTIVE", segment_code: "SEG-001", risk_level: "EXTREME", overdue_days: 11 },
    { id: 2, pipeline_segment_id: 2, point_code: "PT-1002", point_type: "消火栓", address_desc: "城西商业街 12 号门前", check_frequency: "15d", last_checked_at: "2026-09-10T09:00:00", status: "ACTIVE", segment_code: "SEG-002", risk_level: "HIGH", overdue_days: -5 },
    { id: 3, pipeline_segment_id: 3, point_code: "PT-1003", point_type: "水表井", address_desc: "城南小区 3 栋单元口", check_frequency: "30d", last_checked_at: "2026-08-15T09:00:00", status: "ACTIVE", segment_code: "SEG-003", risk_level: "MEDIUM", overdue_days: 5 }
  ],
  leakReport: [
    { id: 1, reporter_type: "INSPECTOR", point_id: 1, leak_level: "BURST", description: "阀门井内水管爆裂，路面大量积水", reported_at: "2026-09-19T08:30:00", verify_status: "PENDING", photo_url: "/mock/photo-1.png", block_reason: "BURST 级漏损未核实，不能派工", point_code: "PT-1001", check_frequency: "7d", last_checked_at: "2026-09-01T09:00:00", segment_id: 1, segment_code: "SEG-001", district: "城东片区", risk_level: "EXTREME", overdue_days: 11 },
    { id: 5, reporter_type: "INSPECTOR", point_id: 5, leak_level: "BURST", description: "路口阀门井爆管，已现场核实", reported_at: "2026-09-19T07:00:00", verify_status: "VERIFIED", photo_url: "/mock/photo-5.png", block_reason: null, point_code: "PT-1005", check_frequency: "7d", last_checked_at: "2026-09-02T09:00:00", segment_id: 5, segment_code: "SEG-005", district: "高新区", risk_level: "HIGH", overdue_days: 10 },
    { id: 2, reporter_type: "RESIDENT", point_id: 2, leak_level: "MAJOR", description: "消火栓接口持续漏水", reported_at: "2026-09-18T10:00:00", verify_status: "VERIFIED", photo_url: "/mock/photo-2.png", block_reason: null, point_code: "PT-1002", check_frequency: "15d", last_checked_at: "2026-09-10T09:00:00", segment_id: 2, segment_code: "SEG-002", district: "城西片区", risk_level: "HIGH", overdue_days: -5 }
  ],
  repairOrder: [
    { id: 1, leak_report_id: 4, crew_id: 2, crew_code: "CREW-02", crew_name: "城西维修二队", leak_level: "TRACE", priority: "P3", status: "ASSIGNED", planned_start: "2026-09-20T08:00:00", finished_at: null, cost_amount: null, dispatch_date: "2026-09-20", block_reason: null, created_at: "2026-09-20T08:05:00" },
    { id: 2, leak_report_id: 7, crew_id: 1, crew_code: "CREW-01", crew_name: "城东维修一队", leak_level: "MINOR", priority: "P2", status: "CLOSED", planned_start: "2026-09-19T09:00:00", finished_at: "2026-09-19T17:00:00", cost_amount: 3200, dispatch_date: "2026-09-19", block_reason: null, created_at: "2026-09-19T08:30:00" }
  ],
  repairCrew: [
    { id: 1, crew_code: "CREW-01", crew_name: "城东维修一队", contact_phone: "13800000001" },
    { id: 2, crew_code: "CREW-02", crew_name: "城西维修二队", contact_phone: "13800000002" },
    { id: 3, crew_code: "CREW-03", crew_name: "应急抢修队", contact_phone: "13800000003" }
  ],
  materialUsage: [
    { id: 1, repair_order_id: 2, material_code: "PIPE-DN150", material_name: "PE 管 DN150", quantity: 2, unit: "根", warehouse: "中心仓库", usage_status: "CONSUMED" },
    { id: 2, repair_order_id: 2, material_code: "SEALANT", material_name: "快速堵漏剂", quantity: 1, unit: "箱", warehouse: "东区仓库", usage_status: "CONSUMED" }
  ],
  materialStock: [
    { id: 1, material_code: "PIPE-DN300", material_name: "球墨铸铁管 DN300", warehouse: "中心仓库", quantity: 20 },
    { id: 2, material_code: "PIPE-DN150", material_name: "PE 管 DN150", warehouse: "中心仓库", quantity: 50 },
    { id: 3, material_code: "VALVE-100", material_name: "闸阀 DN100", warehouse: "东区仓库", quantity: 15 },
    { id: 4, material_code: "SEALANT", material_name: "快速堵漏剂", warehouse: "东区仓库", quantity: 100 }
  ]
} as const;
