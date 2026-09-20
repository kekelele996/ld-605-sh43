package com.generated.waterLeak.constructors;

import com.generated.waterLeak.models.RepairOrder;
import java.util.LinkedHashMap;
import java.util.Map;

public final class RepairOrderDtoFactory {

  public static Map<String, Object> toResponse(RepairOrder order, String crewName, String leakLevel) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("id", order.id);
    body.put("leak_report_id", order.leakReportId);
    body.put("crew_id", order.crewId);
    body.put("crew_name", crewName);
    body.put("leak_level", leakLevel);
    body.put("priority", order.priority);
    body.put("status", order.status);
    body.put("planned_start", order.plannedStart == null ? null : order.plannedStart.toString());
    body.put("work_date", order.workDate == null ? null : order.workDate.toString());
    body.put("finished_at", order.finishedAt == null ? null : order.finishedAt.toString());
    body.put("cost_amount", order.costAmount);
    return body;
  }
}
