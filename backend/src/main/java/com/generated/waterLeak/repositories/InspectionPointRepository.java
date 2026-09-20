package com.generated.waterLeak.repositories;

import com.generated.waterLeak.models.InspectionPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectionPointRepository extends JpaRepository<InspectionPoint, Long> {}
