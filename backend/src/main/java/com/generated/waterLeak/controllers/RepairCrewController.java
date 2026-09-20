package com.generated.waterLeak.controllers;

import com.generated.waterLeak.models.RepairCrew;
import com.generated.waterLeak.routes.RepairCrewRoutes;
import com.generated.waterLeak.services.RepairCrewService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(RepairCrewRoutes.PATH)
public class RepairCrewController {
  private final RepairCrewService service;

  public RepairCrewController(RepairCrewService service) { this.service = service; }

  @GetMapping
  public List<RepairCrew> list() { return service.list(); }
}
