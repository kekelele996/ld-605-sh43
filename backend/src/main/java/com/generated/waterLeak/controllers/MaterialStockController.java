package com.generated.waterLeak.controllers;

import com.generated.waterLeak.models.MaterialStock;
import com.generated.waterLeak.routes.MaterialStockRoutes;
import com.generated.waterLeak.services.MaterialStockService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(MaterialStockRoutes.PATH)
public class MaterialStockController {
  private final MaterialStockService service;

  public MaterialStockController(MaterialStockService service) { this.service = service; }

  @GetMapping
  public List<MaterialStock> list() { return service.list(); }
}
