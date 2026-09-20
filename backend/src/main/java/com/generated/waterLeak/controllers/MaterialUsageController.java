package com.generated.waterLeak.controllers;

import com.generated.waterLeak.routes.MaterialUsageRoutes;
import com.generated.waterLeak.services.MaterialUsageService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(MaterialUsageRoutes.PATH)
public class MaterialUsageController {
  private final MaterialUsageService service;

  public MaterialUsageController(MaterialUsageService service) { this.service = service; }

  @GetMapping
  public List<Map<String, Object>> list(@RequestParam(required = false) Long repairOrderId) {
    return service.list(repairOrderId);
  }

  @GetMapping(MaterialUsageRoutes.STOCK)
  public List<Map<String, Object>> stock() {
    return service.listStock();
  }
}
