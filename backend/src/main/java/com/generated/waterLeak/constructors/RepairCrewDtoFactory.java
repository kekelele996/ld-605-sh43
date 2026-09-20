package com.generated.waterLeak.constructors;

import com.generated.waterLeak.models.RepairCrew;
import java.util.LinkedHashMap;
import java.util.Map;

/** 维修队响应构造器。 */
public final class RepairCrewDtoFactory {

  private RepairCrewDtoFactory() {}

  public static Map<String, Object> toResponse(RepairCrew crew) {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("id", crew.getId());
    map.put("crew_code", crew.getCrewCode());
    map.put("crew_name", crew.getCrewName());
    map.put("contact_phone", crew.getContactPhone());
    return map;
  }
}
