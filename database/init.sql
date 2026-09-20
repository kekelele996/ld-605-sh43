-- 城市水务漏损巡检平台：风险联巡 → 维修验收
-- 说明：所有种子数据使用 CURRENT_DATE 相对时间，保证任意日期初始化后
--       “巡检超期”“同一维修队同一天未关闭单” 等规则都有可演示的数据。

CREATE TABLE IF NOT EXISTS pipeline_segment (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  segment_code TEXT NOT NULL,
  district TEXT,
  material TEXT,
  diameter TEXT,
  install_year INTEGER,
  pressure_zone TEXT,
  risk_level TEXT NOT NULL DEFAULT 'LOW'
);

CREATE TABLE IF NOT EXISTS inspection_point (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  pipeline_segment_id BIGINT NOT NULL REFERENCES pipeline_segment(id),
  point_code TEXT NOT NULL,
  point_type TEXT,
  address_desc TEXT,
  check_frequency TEXT NOT NULL DEFAULT 'MONTHLY',
  last_checked_at TIMESTAMP,
  status TEXT NOT NULL DEFAULT 'OPEN'
);

CREATE TABLE IF NOT EXISTS leak_report (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  reporter_type TEXT NOT NULL DEFAULT 'RESIDENT',
  point_id BIGINT NOT NULL REFERENCES inspection_point(id),
  leak_level TEXT NOT NULL,
  description TEXT,
  reported_at TIMESTAMP NOT NULL DEFAULT now(),
  verify_status TEXT NOT NULL DEFAULT 'PENDING',
  photo_url TEXT
);

CREATE TABLE IF NOT EXISTS repair_crew (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  crew_code TEXT NOT NULL UNIQUE,
  crew_name TEXT NOT NULL,
  status TEXT NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE IF NOT EXISTS repair_order (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  leak_report_id BIGINT NOT NULL REFERENCES leak_report(id),
  crew_id BIGINT NOT NULL REFERENCES repair_crew(id),
  priority TEXT,
  status TEXT NOT NULL DEFAULT 'ASSIGNED',
  planned_start TIMESTAMP,
  work_date DATE NOT NULL,
  finished_at TIMESTAMP,
  cost_amount NUMERIC(12,2)
);

-- 同一维修队同一天只能有一张未关闭维修单：部分唯一索引兜底并发派工。
CREATE UNIQUE INDEX IF NOT EXISTS ux_repair_order_crew_day_open
  ON repair_order (crew_id, work_date)
  WHERE status <> 'CLOSED';

CREATE TABLE IF NOT EXISTS material_stock (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  material_code TEXT NOT NULL,
  material_name TEXT NOT NULL,
  unit TEXT,
  warehouse TEXT NOT NULL,
  quantity INTEGER NOT NULL DEFAULT 0,
  CONSTRAINT ux_material_stock_wh_code UNIQUE (warehouse, material_code)
);

CREATE TABLE IF NOT EXISTS material_usage (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  repair_order_id BIGINT NOT NULL REFERENCES repair_order(id),
  material_code TEXT NOT NULL,
  material_name TEXT,
  quantity INTEGER NOT NULL,
  unit TEXT,
  warehouse TEXT NOT NULL,
  usage_status TEXT NOT NULL DEFAULT 'PICKED'
);

CREATE TABLE IF NOT EXISTS audit_log (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  actor TEXT,
  action TEXT NOT NULL,
  target_type TEXT,
  target_id BIGINT,
  detail TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT now()
);

-- ============ 种子数据（仅空库时写入） ============

INSERT INTO pipeline_segment (segment_code, district, material, diameter, install_year, pressure_zone, risk_level)
SELECT * FROM (VALUES
  ('SEG-101', '城东', '球墨铸铁', 'DN300', 1998, 'PZ-1', 'EXTREME'),
  ('SEG-102', '城西', 'PE',       'DN200', 2008, 'PZ-2', 'HIGH'),
  ('SEG-103', '城南', '钢管',     'DN150', 2015, 'PZ-2', 'MEDIUM'),
  ('SEG-104', '城北', 'PE',       'DN110', 2021, 'PZ-3', 'LOW')
) AS v(segment_code, district, material, diameter, install_year, pressure_zone, risk_level)
WHERE NOT EXISTS (SELECT 1 FROM pipeline_segment);

INSERT INTO inspection_point (pipeline_segment_id, point_code, point_type, address_desc, check_frequency, last_checked_at, status)
SELECT * FROM (VALUES
  ((SELECT id FROM pipeline_segment WHERE segment_code='SEG-101'), 'PT-1001', 'VALVE',  '城东路口东侧井', 'WEEKLY',    now() - INTERVAL '20 days',  'OPEN'),
  ((SELECT id FROM pipeline_segment WHERE segment_code='SEG-102'), 'PT-1002', 'HYDRANT','城西商业街 12 号', 'WEEKLY',    now() - INTERVAL '3 days',   'OPEN'),
  ((SELECT id FROM pipeline_segment WHERE segment_code='SEG-103'), 'PT-1003', 'METER',  '城南小区表井',   'MONTHLY',   now() - INTERVAL '45 days',  'OPEN'),
  ((SELECT id FROM pipeline_segment WHERE segment_code='SEG-104'), 'PT-1004', 'VALVE',  '城北工业园西门', 'DAILY',     now(),                       'OPEN'),
  ((SELECT id FROM pipeline_segment WHERE segment_code='SEG-101'), 'PT-1005', 'METER',  '城东老桥下',     'QUARTERLY', now() - INTERVAL '100 days', 'OPEN')
) AS v(pipeline_segment_id, point_code, point_type, address_desc, check_frequency, last_checked_at, status)
WHERE NOT EXISTS (SELECT 1 FROM inspection_point);

INSERT INTO leak_report (reporter_type, point_id, leak_level, description, reported_at, verify_status, photo_url)
SELECT * FROM (VALUES
  ('INSPECTOR', (SELECT id FROM inspection_point WHERE point_code='PT-1001'), 'BURST', '路面涌水，疑似主管爆裂',   now() - INTERVAL '6 hours',  'PENDING',  '/mock/burst-1.jpg'),
  ('RESIDENT',  (SELECT id FROM inspection_point WHERE point_code='PT-1002'), 'BURST', '商铺前大量冒水',           now() - INTERVAL '5 hours',  'VERIFIED', '/mock/burst-2.jpg'),
  ('INSPECTOR', (SELECT id FROM inspection_point WHERE point_code='PT-1003'), 'MAJOR', '表井持续渗水',             now() - INTERVAL '2 days',   'VERIFIED', '/mock/major-1.jpg'),
  ('RESIDENT',  (SELECT id FROM inspection_point WHERE point_code='PT-1004'), 'MINOR', '绿化带长期潮湿',           now() - INTERVAL '3 days',   'PENDING',  '/mock/minor-1.jpg'),
  ('INSPECTOR', (SELECT id FROM inspection_point WHERE point_code='PT-1005'), 'TRACE', '夜间听漏有轻微异响',       now() - INTERVAL '4 days',   'VERIFIED', '/mock/trace-1.jpg')
) AS v(reporter_type, point_id, leak_level, description, reported_at, verify_status, photo_url)
WHERE NOT EXISTS (SELECT 1 FROM leak_report);

INSERT INTO repair_crew (crew_code, crew_name, status)
SELECT * FROM (VALUES
  ('CREW-A', '抢修一队', 'ACTIVE'),
  ('CREW-B', '抢修二队', 'ACTIVE'),
  ('CREW-C', '巡检维修队', 'ACTIVE')
) AS v(crew_code, crew_name, status)
WHERE NOT EXISTS (SELECT 1 FROM repair_crew);

-- 抢修一队今天已有一张未关闭维修单：用于演示“同一维修队同一天只成功一单”。
INSERT INTO repair_order (leak_report_id, crew_id, priority, status, planned_start, work_date, cost_amount)
SELECT lr.id, c.id, 'MAJOR', 'ASSIGNED', now(), CURRENT_DATE, NULL
FROM leak_report lr, repair_crew c
WHERE lr.leak_level = 'MAJOR' AND lr.verify_status = 'VERIFIED'
  AND c.crew_code = 'CREW-A'
  AND NOT EXISTS (SELECT 1 FROM repair_order);

INSERT INTO material_stock (material_code, material_name, unit, warehouse, quantity)
SELECT * FROM (VALUES
  ('MAT-PIPE-DN300', '球墨铸铁管 DN300', '根', 'WH-01', 12),
  ('MAT-VALVE-DN300', '闸阀 DN300',      '个', 'WH-01', 20),
  ('MAT-SEALANT',     '管道密封胶',       '支', 'WH-01', 50),
  ('MAT-PIPE-DN300', '球墨铸铁管 DN300', '根', 'WH-02', 5)
) AS v(material_code, material_name, unit, warehouse, quantity)
WHERE NOT EXISTS (SELECT 1 FROM material_stock);
