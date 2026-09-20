package com.generated.waterLeak.services;

import com.generated.waterLeak.constructors.MaterialStockDtoFactory;
import com.generated.waterLeak.constructors.MaterialUsageDtoFactory;
import com.generated.waterLeak.repositories.MaterialStockRepository;
import com.generated.waterLeak.repositories.MaterialUsageRepository;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaterialUsageService {
  private final MaterialUsageRepository repo;
  private final MaterialStockRepository stockRepo;

  public MaterialUsageService(MaterialUsageRepository repo, MaterialStockRepository stockRepo) {
    this.repo = repo;
    this.stockRepo = stockRepo;
  }

  @Transactional(readOnly = true)
  public List<Map<String, Object>> list(Long repairOrderId) {
    var rows = repairOrderId == null ? repo.findAll() : repo.findByRepairOrderIdOrderByIdAsc(repairOrderId);
    return rows.stream().map(MaterialUsageDtoFactory::toResponse).toList();
  }

  @Transactional(readOnly = true)
  public List<Map<String, Object>> listStock() {
    return stockRepo.findAll().stream().map(MaterialStockDtoFactory::toResponse).toList();
  }
}
