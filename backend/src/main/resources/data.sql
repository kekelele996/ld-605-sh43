-- 本地 H2(local profile)种子数据，与 database/init.sql 中的 PostgreSQL 种子保持一致。
-- 仅嵌入式数据源执行（spring.sql.init.mode=embedded），生产 PostgreSQL 由 init.sql 负责。

INSERT INTO pipeline_segment (id, segment_code, district, material, diameter, install_year, pressure_zone, risk_level) VALUES
  (1, 'SEG-001', '城东片区', '铸铁',   'DN300', '1998', '高压区',   'EXTREME'),
  (2, 'SEG-002', '城西片区', 'PE',     'DN200', '2010', '中压区',   'HIGH'),
  (3, 'SEG-003', '城南片区', '钢管',   'DN150', '2015', '中压区',   'MEDIUM'),
  (4, 'SEG-004', '城北片区', 'PE',     'DN100', '2020', '低压区',   'LOW'),
  (5, 'SEG-005', '高新区',   '球墨铸铁', 'DN250', '2005', '高压区',   'HIGH');

INSERT INTO inspection_point (id, pipeline_segment_id, point_code, point_type, address_desc, check_frequency, last_checked_at, status) VALUES
  (1, 1, 'PT-1001', '阀门井',   '城东路口西北角阀门井', '7d',  '2026-09-01 09:00:00', 'ACTIVE'),
  (2, 2, 'PT-1002', '消火栓',   '城西商业街 12 号门前', '15d', '2026-09-10 09:00:00', 'ACTIVE'),
  (3, 3, 'PT-1003', '水表井',   '城南小区 3 栋单元口',   '30d', '2026-08-15 09:00:00', 'ACTIVE'),
  (4, 4, 'PT-1004', '排气阀',   '城北立交辅道绿化带',   '30d', '2026-09-15 09:00:00', 'ACTIVE'),
  (5, 5, 'PT-1005', '阀门井',   '高新区创业路口',       '7d',  '2026-09-02 09:00:00', 'ACTIVE'),
  (6, 1, 'PT-1006', '流量计井', '城东水厂出厂管段',     '15d', '2026-09-01 09:00:00', 'ACTIVE');

INSERT INTO leak_report (id, reporter_type, point_id, leak_level, description, reported_at, verify_status, photo_url, block_reason) VALUES
  (1, 'INSPECTOR', 1, 'BURST', '阀门井内水管爆裂，路面大量积水', '2026-09-19 08:30:00', 'PENDING',  '/mock/photo-1.png', NULL),
  (2, 'RESIDENT',  2, 'MAJOR', '消火栓接口持续漏水',           '2026-09-18 10:00:00', 'VERIFIED', '/mock/photo-2.png', NULL),
  (3, 'INSPECTOR', 3, 'MINOR', '水表井内渗水，井底有积水',     '2026-09-18 15:00:00', 'PENDING',  '/mock/photo-3.png', NULL),
  (4, 'RESIDENT',  4, 'TRACE', '绿化带土壤长期潮湿疑似暗漏',   '2026-09-17 09:00:00', 'VERIFIED', '/mock/photo-4.png', NULL),
  (5, 'INSPECTOR', 5, 'BURST', '路口阀门井爆管，已现场核实',   '2026-09-19 07:00:00', 'VERIFIED', '/mock/photo-5.png', NULL),
  (6, 'RESIDENT',  6, 'MAJOR', '出厂管段流量计井渗水',         '2026-09-19 11:00:00', 'PENDING',  '/mock/photo-6.png', NULL),
  (7, 'INSPECTOR', 3, 'MINOR', '历史渗水点复查上报',           '2026-09-10 09:00:00', 'VERIFIED', '/mock/photo-7.png', NULL);

INSERT INTO repair_crew (id, crew_code, crew_name, contact_phone) VALUES
  (1, 'CREW-01', '城东维修一队', '13800000001'),
  (2, 'CREW-02', '城西维修二队', '13800000002'),
  (3, 'CREW-03', '应急抢修队',   '13800000003');

-- 维修二队当天已有一张未关闭维修单（演示“同一维修队同一天只能有一张未关闭维修单”）
INSERT INTO repair_order (id, leak_report_id, crew_id, priority, status, planned_start, finished_at, cost_amount, dispatch_date, block_reason, created_at) VALUES
  (1, 4, 2, 'P3', 'ASSIGNED', CURRENT_TIMESTAMP, NULL, NULL, CURRENT_DATE, NULL, CURRENT_TIMESTAMP),
  (2, 7, 1, 'P2', 'CLOSED',   DATE '2026-09-19' + TIME '09:00:00', '2026-09-19 17:00:00', 3200.00, DATE '2026-09-19', NULL, '2026-09-19 08:30:00');

INSERT INTO material_usage (id, repair_order_id, material_code, material_name, quantity, unit, warehouse, usage_status) VALUES
  (1, 2, 'PIPE-DN150', 'PE 管 DN150', 2, '根', '中心仓库', 'CONSUMED'),
  (2, 2, 'SEALANT',    '快速堵漏剂',  1, '箱', '东区仓库', 'CONSUMED');

INSERT INTO material_stock (id, material_code, material_name, warehouse, quantity) VALUES
  (1, 'PIPE-DN300', '球墨铸铁管 DN300', '中心仓库', 20),
  (2, 'PIPE-DN150', 'PE 管 DN150',      '中心仓库', 50),
  (3, 'VALVE-100',  '闸阀 DN100',       '东区仓库', 15),
  (4, 'SEALANT',    '快速堵漏剂',        '东区仓库', 100);

ALTER TABLE pipeline_segment ALTER COLUMN id RESTART WITH 100;
ALTER TABLE inspection_point ALTER COLUMN id RESTART WITH 100;
ALTER TABLE leak_report ALTER COLUMN id RESTART WITH 100;
ALTER TABLE repair_crew ALTER COLUMN id RESTART WITH 100;
ALTER TABLE repair_order ALTER COLUMN id RESTART WITH 100;
ALTER TABLE material_usage ALTER COLUMN id RESTART WITH 100;
ALTER TABLE material_stock ALTER COLUMN id RESTART WITH 100;
ALTER TABLE audit_log ALTER COLUMN id RESTART WITH 100;
