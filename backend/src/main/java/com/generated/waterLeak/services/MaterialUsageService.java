package com.generated.waterLeak.services;

import com.generated.waterLeak.constructors.MaterialUsageDtoFactory;
import com.generated.waterLeak.repositories.MaterialUsageRepository;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class MaterialUsageService {
  private final MaterialUsageRepository repo;

  public MaterialUsageService(MaterialUsageRepository repo) { this.repo = repo; }

  public List<Map<String, Object>> list(Long orderId) {
    if (orderId == null) {
      return repo.findAll().stream().map(MaterialUsageDtoFactory::toResponse).toList();
    }
    return repo.findByRepairOrderIdOrderByIdAsc(orderId).stream()
        .map(MaterialUsageDtoFactory::toResponse).toList();
  }
}
