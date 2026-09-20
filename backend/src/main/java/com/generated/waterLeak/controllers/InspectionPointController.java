package com.generated.waterLeak.controllers;

import com.generated.waterLeak.models.InspectionPoint;
import com.generated.waterLeak.routes.InspectionPointRoutes;
import com.generated.waterLeak.services.InspectionPointService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(InspectionPointRoutes.PATH)
public class InspectionPointController {
  private final InspectionPointService service;

  public InspectionPointController(InspectionPointService service) { this.service = service; }

  @GetMapping
  public List<InspectionPoint> list() { return service.list(); }
}
