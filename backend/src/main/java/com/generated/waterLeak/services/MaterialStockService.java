package com.generated.waterLeak.services;

import com.generated.waterLeak.models.MaterialStock;
import com.generated.waterLeak.repositories.MaterialStockRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MaterialStockService {
  private final MaterialStockRepository repo;

  public MaterialStockService(MaterialStockRepository repo) { this.repo = repo; }

  public List<MaterialStock> list() { return repo.findAll(); }
}
