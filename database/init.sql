CREATE TABLE IF NOT EXISTS pipeline_segment (
  id INTEGER PRIMARY KEY,
  segment_code TEXT,
  district TEXT,
  material TEXT,
  diameter TEXT,
  install_year TEXT,
  pressure_zone TEXT,
  risk_level TEXT
);

CREATE TABLE IF NOT EXISTS inspection_point (
  id INTEGER PRIMARY KEY,
  pipeline_segment_id TEXT,
  point_code TEXT,
  point_type TEXT,
  address_desc TEXT,
  check_frequency TEXT,
  last_checked_at TEXT,
  status TEXT
);

CREATE TABLE IF NOT EXISTS leak_report (
  id INTEGER PRIMARY KEY,
  reporter_type TEXT,
  point_id TEXT,
  leak_level TEXT,
  description TEXT,
  reported_at TEXT,
  verify_status TEXT,
  photo_url TEXT
);

CREATE TABLE IF NOT EXISTS repair_order (
  id INTEGER PRIMARY KEY,
  leak_report_id TEXT,
  crew_id TEXT,
  priority TEXT,
  status TEXT,
  planned_start TEXT,
  finished_at TEXT,
  cost_amount TEXT
);

CREATE TABLE IF NOT EXISTS material_usage (
  id INTEGER PRIMARY KEY,
  repair_order_id TEXT,
  material_code TEXT,
  material_name TEXT,
  quantity TEXT,
  unit TEXT,
  warehouse TEXT,
  usage_status TEXT
);

CREATE TABLE IF NOT EXISTS audit_log (
  id INTEGER PRIMARY KEY,
  actor TEXT,
  action TEXT,
  target_type TEXT,
  target_id TEXT,
  created_at TEXT
);
