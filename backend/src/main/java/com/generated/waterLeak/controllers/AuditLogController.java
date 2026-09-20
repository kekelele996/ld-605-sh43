package com.generated.waterLeak.controllers;

import com.generated.waterLeak.models.AuditLog;
import com.generated.waterLeak.routes.AuditLogRoutes;
import com.generated.waterLeak.services.AuditLogService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AuditLogRoutes.PATH)
public class AuditLogController {
  private final AuditLogService service;

  public AuditLogController(AuditLogService service) { this.service = service; }

  @GetMapping
  public List<AuditLog> list() { return service.listRecent(); }
}
