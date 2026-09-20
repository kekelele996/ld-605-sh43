package com.generated.waterLeak.repositories;

import com.generated.waterLeak.models.InspectionPoint;
import com.generated.waterLeak.types.InspectionPointView;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectionPointRepository extends JpaRepository<InspectionPoint, Long> {

  @Query("""
    select new com.generated.waterLeak.types.InspectionPointView(
      p.id, p.pipelineSegmentId, p.pointCode, p.pointType, p.addressDesc,
      p.checkFrequency, p.lastCheckedAt, p.status, s.segmentCode, s.riskLevel
    )
    from InspectionPoint p
    join PipelineSegment s on p.pipelineSegmentId = s.id
    """)
  List<InspectionPointView> findAllJoined();
}
