package com.generated.waterLeak.repositories;

import com.generated.waterLeak.models.RepairOrder;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairOrderRepository extends JpaRepository<RepairOrder, Long> {

  List<RepairOrder> findByLeakReportIdAndStatusNot(Long leakReportId, String status);

  List<RepairOrder> findByCrewIdAndWorkDateAndStatusNot(Long crewId, LocalDate workDate, String status);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select o from RepairOrder o where o.id = :id")
  Optional<RepairOrder> findByIdForUpdate(@Param("id") Long id);
}
