package com.generated.waterLeak.controllers;

import com.generated.waterLeak.models.PipelineSegment;
import com.generated.waterLeak.routes.PipelineSegmentRoutes;
import com.generated.waterLeak.services.PipelineSegmentService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PipelineSegmentRoutes.PATH)
public class PipelineSegmentController {
  private final PipelineSegmentService service;

  public PipelineSegmentController(PipelineSegmentService service) { this.service = service; }

  @GetMapping
  public List<PipelineSegment> list() { return service.list(); }
}
