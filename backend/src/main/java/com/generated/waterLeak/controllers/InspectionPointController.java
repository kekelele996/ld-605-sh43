package com.generated.waterLeak.controllers;

import com.generated.waterLeak.services.InspectionPointService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inspection-point")
public class InspectionPointController {
  private final InspectionPointService service;

  public InspectionPointController(InspectionPointService service) { this.service = service; }

  @GetMapping
  public List<Map<String, Object>> list() {
    return service.list();
  }
}
