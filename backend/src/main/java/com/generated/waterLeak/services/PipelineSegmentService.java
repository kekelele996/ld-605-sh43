package com.generated.waterLeak.services;

import com.generated.waterLeak.models.PipelineSegment;
import com.generated.waterLeak.repositories.PipelineSegmentRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PipelineSegmentService {
  private final PipelineSegmentRepository repo;

  public PipelineSegmentService(PipelineSegmentRepository repo) { this.repo = repo; }

  public List<PipelineSegment> list() { return repo.findAll(); }
}
