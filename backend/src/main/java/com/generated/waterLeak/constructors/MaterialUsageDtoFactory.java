package com.generated.waterLeak.constructors;

import com.generated.waterLeak.models.MaterialUsage;
import java.util.LinkedHashMap;
import java.util.Map;

public final class MaterialUsageDtoFactory {

  public static Map<String, Object> toResponse(MaterialUsage usage) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("id", usage.id);
    body.put("repair_order_id", usage.repairOrderId);
    body.put("material_code", usage.materialCode);
    body.put("material_name", usage.materialName);
    body.put("quantity", usage.quantity);
    body.put("unit", usage.unit);
    body.put("warehouse", usage.warehouse);
    body.put("usage_status", usage.usageStatus);
    return body;
  }
}
