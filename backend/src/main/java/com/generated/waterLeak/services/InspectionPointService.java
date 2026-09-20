package com.generated.waterLeak.services;

import com.generated.waterLeak.models.InspectionPoint;
import com.generated.waterLeak.repositories.InspectionPointRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class InspectionPointService {
  private final InspectionPointRepository repo;

  public InspectionPointService(InspectionPointRepository repo) { this.repo = repo; }

  public List<InspectionPoint> list() { return repo.findAll(); }
}
