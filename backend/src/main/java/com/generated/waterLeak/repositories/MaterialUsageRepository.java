package com.generated.waterLeak.repositories;

import com.generated.waterLeak.models.MaterialUsage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialUsageRepository extends JpaRepository<MaterialUsage, Long> {
  List<MaterialUsage> findByRepairOrderIdOrderByIdAsc(Long repairOrderId);
}
