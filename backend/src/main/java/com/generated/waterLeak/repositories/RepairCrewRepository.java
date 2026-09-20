package com.generated.waterLeak.repositories;

import com.generated.waterLeak.models.RepairCrew;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairCrewRepository extends JpaRepository<RepairCrew, Long> {

  /**
   * 派工时悲观锁维修队行：把同一维修队的并发派工串行化，
   * 配合 repair_order 上 (crew_id, dispatch_date) 未关闭唯一索引，保证并发只成功一单。
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select c from RepairCrew c where c.id = :id")
  Optional<RepairCrew> findByIdForUpdate(@Param("id") Long id);
}
