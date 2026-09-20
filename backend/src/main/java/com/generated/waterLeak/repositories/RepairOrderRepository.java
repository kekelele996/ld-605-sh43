package com.generated.waterLeak.repositories;

import com.generated.waterLeak.models.RepairOrder;
import com.generated.waterLeak.types.RepairOrderView;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairOrderRepository extends JpaRepository<RepairOrder, Long> {

  /** 同一维修队某一天未关闭（status <> CLOSED）维修单数量。 */
  long countByCrewIdAndDispatchDateAndStatusNot(Long crewId, LocalDate dispatchDate, String status);

  @Query("""
    select new com.generated.waterLeak.types.RepairOrderView(
      o.id, o.leakReportId, o.crewId, c.crewCode, c.crewName, r.leakLevel,
      o.priority, o.status, o.plannedStart, o.finishedAt, o.costAmount,
      o.dispatchDate, o.blockReason, o.createdAt
    )
    from RepairOrder o
    join RepairCrew c on o.crewId = c.id
    left join LeakReport r on o.leakReportId = r.id
    order by o.id desc
    """)
  List<RepairOrderView> findAllJoined();

  @Query("select o from RepairOrder o where o.leakReportId = :reportId and o.status <> 'CLOSED'")
  List<RepairOrder> findOpenByReportId(@Param("reportId") Long reportId);
}
