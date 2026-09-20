export const mockData = {
  "pipelineSegment": [
    {
      "id": 1,
      "segment_code": "segment code 1",
      "district": "district 1",
      "material": "material 1",
      "diameter": "diameter 1",
      "install_year": "install year 1",
      "pressure_zone": "pressure zone 1",
      "risk_level": "LOW"
    },
    {
      "id": 2,
      "segment_code": "segment code 2",
      "district": "district 2",
      "material": "material 2",
      "diameter": "diameter 2",
      "install_year": "install year 2",
      "pressure_zone": "pressure zone 2",
      "risk_level": "MEDIUM"
    },
    {
      "id": 3,
      "segment_code": "segment code 3",
      "district": "district 3",
      "material": "material 3",
      "diameter": "diameter 3",
      "install_year": "install year 3",
      "pressure_zone": "pressure zone 3",
      "risk_level": "HIGH"
    }
  ],
  "inspectionPoint": [
    {
      "id": 1,
      "pipeline_segment_id": 1,
      "point_code": "point code 1",
      "point_type": "MINOR",
      "address_desc": "address desc 1",
      "check_frequency": "check frequency 1",
      "last_checked_at": "2026-06-11T09:00:00Z",
      "status": "ASSIGNED"
    },
    {
      "id": 2,
      "pipeline_segment_id": 2,
      "point_code": "point code 2",
      "point_type": "MAJOR",
      "address_desc": "address desc 2",
      "check_frequency": "check frequency 2",
      "last_checked_at": "2026-06-12T09:00:00Z",
      "status": "WORKING"
    },
    {
      "id": 3,
      "pipeline_segment_id": 3,
      "point_code": "point code 3",
      "point_type": "BURST",
      "address_desc": "address desc 3",
      "check_frequency": "check frequency 3",
      "last_checked_at": "2026-06-13T09:00:00Z",
      "status": "WAIT_ASSIGN"
    }
  ],
  "leakReport": [
    {
      "id": 1,
      "reporter_type": "MINOR",
      "point_id": 1,
      "leak_level": "LOW",
      "description": "description 1",
      "reported_at": "2026-06-11T09:00:00Z",
      "verify_status": "ASSIGNED",
      "photo_url": "/mock/photo_url-1.png"
    },
    {
      "id": 2,
      "reporter_type": "MAJOR",
      "point_id": 2,
      "leak_level": "MEDIUM",
      "description": "description 2",
      "reported_at": "2026-06-12T09:00:00Z",
      "verify_status": "WORKING",
      "photo_url": "/mock/photo_url-2.png"
    },
    {
      "id": 3,
      "reporter_type": "BURST",
      "point_id": 3,
      "leak_level": "HIGH",
      "description": "description 3",
      "reported_at": "2026-06-13T09:00:00Z",
      "verify_status": "WAIT_ASSIGN",
      "photo_url": "/mock/photo_url-3.png"
    }
  ],
  "repairOrder": [
    {
      "id": 1,
      "leak_report_id": 1,
      "crew_id": 1,
      "priority": "priority 1",
      "status": "ASSIGNED",
      "planned_start": "planned start 1",
      "finished_at": "2026-06-11T09:00:00Z",
      "cost_amount": 15400
    },
    {
      "id": 2,
      "leak_report_id": 2,
      "crew_id": 2,
      "priority": "priority 2",
      "status": "WORKING",
      "planned_start": "planned start 2",
      "finished_at": "2026-06-12T09:00:00Z",
      "cost_amount": 18800
    },
    {
      "id": 3,
      "leak_report_id": 3,
      "crew_id": 3,
      "priority": "priority 3",
      "status": "WAIT_ASSIGN",
      "planned_start": "planned start 3",
      "finished_at": "2026-06-13T09:00:00Z",
      "cost_amount": 22200
    }
  ],
  "materialUsage": [
    {
      "id": 1,
      "repair_order_id": 1,
      "material_code": "material code 1",
      "material_name": "material name 1",
      "quantity": 92,
      "unit": "unit 1",
      "warehouse": "warehouse 1",
      "usage_status": "ASSIGNED"
    },
    {
      "id": 2,
      "repair_order_id": 2,
      "material_code": "material code 2",
      "material_name": "material name 2",
      "quantity": 104,
      "unit": "unit 2",
      "warehouse": "warehouse 2",
      "usage_status": "WORKING"
    },
    {
      "id": 3,
      "repair_order_id": 3,
      "material_code": "material code 3",
      "material_name": "material name 3",
      "quantity": 116,
      "unit": "unit 3",
      "warehouse": "warehouse 3",
      "usage_status": "WAIT_ASSIGN"
    }
  ]
} as const;
