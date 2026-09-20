package com.generated.waterLeak.services;

import com.generated.waterLeak.constructors.InspectionPointDtoFactory;
import com.generated.waterLeak.repositories.InspectionPointRepository;
import com.generated.waterLeak.utils.Formatters;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InspectionPointService {
  private final InspectionPointRepository repo;

  public InspectionPointService(InspectionPointRepository repo) { this.repo = repo; }

  /** 巡检点列表：联查管段风险，超期天数大的靠前。 */
  @Transactional(readOnly = true)
  public List<Map<String, Object>> list() {
    LocalDateTime now = LocalDateTime.now();
    return repo.findAllJoined().stream()
      .map(v -> InspectionPointDtoFactory.toResponse(v, Formatters.overdueDays(v.lastCheckedAt(), v.checkFrequency(), now)))
      .sorted(Comparator.comparingLong((Map<String, Object> m) -> (long) m.get("overdue_days")).reversed())
      .toList();
  }
}
