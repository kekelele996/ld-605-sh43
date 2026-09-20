package com.generated.waterLeak.constructors;

import com.generated.waterLeak.types.RepairOrderView;
import java.util.LinkedHashMap;
import java.util.Map;

/** 维修单响应构造器。 */
public final class RepairOrderDtoFactory {

  private RepairOrderDtoFactory() {}

  public static Map<String, Object> toResponse(RepairOrderView view) {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("id", view.id());
    map.put("leak_report_id", view.leakReportId());
    map.put("crew_id", view.crewId());
    map.put("crew_code", view.crewCode());
    map.put("crew_name", view.crewName());
    map.put("leak_level", view.leakLevel());
    map.put("priority", view.priority());
    map.put("status", view.status());
    map.put("planned_start", view.plannedStart());
    map.put("finished_at", view.finishedAt());
    map.put("cost_amount", view.costAmount());
    map.put("dispatch_date", view.dispatchDate());
    map.put("block_reason", view.blockReason());
    map.put("created_at", view.createdAt());
    return map;
  }
}
