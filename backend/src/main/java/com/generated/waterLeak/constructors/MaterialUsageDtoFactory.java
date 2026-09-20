package com.generated.waterLeak.constructors;

import com.generated.waterLeak.models.MaterialUsage;
import java.util.LinkedHashMap;
import java.util.Map;

/** 维修材料响应构造器。 */
public final class MaterialUsageDtoFactory {

  private MaterialUsageDtoFactory() {}

  public static Map<String, Object> toResponse(MaterialUsage usage) {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("id", usage.getId());
    map.put("repair_order_id", usage.getRepairOrderId());
    map.put("material_code", usage.getMaterialCode());
    map.put("material_name", usage.getMaterialName());
    map.put("quantity", usage.getQuantity());
    map.put("unit", usage.getUnit());
    map.put("warehouse", usage.getWarehouse());
    map.put("usage_status", usage.getUsageStatus());
    return map;
  }
}
