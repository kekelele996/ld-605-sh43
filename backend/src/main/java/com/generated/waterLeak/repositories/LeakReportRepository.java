package com.generated.waterLeak.repositories;

import com.generated.waterLeak.models.LeakReport;
import com.generated.waterLeak.types.LeakReportView;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LeakReportRepository extends JpaRepository<LeakReport, Long> {

  /** 联查漏损报告 + 巡检点 + 管段，排序规则在 service 层按等级/风险/超期计算。 */
  @Query("""
    select new com.generated.waterLeak.types.LeakReportView(
      r.id, r.reporterType, r.pointId, r.leakLevel, r.description, r.reportedAt,
      r.verifyStatus, r.photoUrl, r.blockReason,
      p.pointCode, p.checkFrequency, p.lastCheckedAt,
      s.id, s.segmentCode, s.district, s.riskLevel
    )
    from LeakReport r
    join InspectionPoint p on r.pointId = p.id
    join PipelineSegment s on p.pipelineSegmentId = s.id
    """)
  List<LeakReportView> findAllJoined();
}
