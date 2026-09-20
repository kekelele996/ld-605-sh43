package com.generated.waterLeak.repositories;

import com.generated.waterLeak.models.LeakReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeakReportRepository extends JpaRepository<LeakReport, Long> {}
