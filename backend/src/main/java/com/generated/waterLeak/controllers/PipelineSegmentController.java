package com.generated.waterLeak.controllers;

import com.generated.waterLeak.services.PipelineSegmentService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pipeline-segment")
public class PipelineSegmentController {
  private final PipelineSegmentService service;

  public PipelineSegmentController(PipelineSegmentService service) { this.service = service; }

  @GetMapping
  public List<Map<String, Object>> list() {
    return service.list();
  }
}
