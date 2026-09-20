package com.generated.waterLeak.services;

import com.generated.waterLeak.constructors.PipelineSegmentDtoFactory;
import com.generated.waterLeak.repositories.PipelineSegmentRepository;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PipelineSegmentService {
  private final PipelineSegmentRepository repo;

  public PipelineSegmentService(PipelineSegmentRepository repo) { this.repo = repo; }

  @Transactional(readOnly = true)
  public List<Map<String, Object>> list() {
    return repo.findAll().stream().map(PipelineSegmentDtoFactory::toResponse).toList();
  }
}
