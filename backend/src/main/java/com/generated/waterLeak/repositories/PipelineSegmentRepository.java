package com.generated.waterLeak.repositories;

import com.generated.waterLeak.models.PipelineSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PipelineSegmentRepository extends JpaRepository<PipelineSegment, Long> {}
