package com.generated.waterLeak.repositories;

import com.generated.waterLeak.models.AuditLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
  List<AuditLog> findTop50ByOrderByIdDesc();
}
