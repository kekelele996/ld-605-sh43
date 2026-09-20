package com.generated.waterLeak.constructors;

import com.generated.waterLeak.models.MaterialStock;
import java.util.LinkedHashMap;
import java.util.Map;

/** 材料库存响应构造器。 */
public final class MaterialStockDtoFactory {

  private MaterialStockDtoFactory() {}

  public static Map<String, Object> toResponse(MaterialStock stock) {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("id", stock.getId());
    map.put("material_code", stock.getMaterialCode());
    map.put("material_name", stock.getMaterialName());
    map.put("warehouse", stock.getWarehouse());
    map.put("quantity", stock.getQuantity());
    return map;
  }
}
