package com.generated.waterLeak.services;

import com.generated.waterLeak.models.RepairCrew;
import com.generated.waterLeak.repositories.RepairCrewRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RepairCrewService {
  private final RepairCrewRepository repo;

  public RepairCrewService(RepairCrewRepository repo) { this.repo = repo; }

  public List<RepairCrew> list() { return repo.findAll(); }
}
