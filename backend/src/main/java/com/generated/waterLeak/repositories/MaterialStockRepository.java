package com.generated.waterLeak.repositories;

import com.generated.waterLeak.models.MaterialStock;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialStockRepository extends JpaRepository<MaterialStock, Long> {

  /** 领料扣减库存时悲观锁库存行，与验收同事务提交或回滚。 */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select s from MaterialStock s where s.materialCode = :code and s.warehouse = :warehouse")
  Optional<MaterialStock> findByCodeAndWarehouseForUpdate(@Param("code") String code, @Param("warehouse") String warehouse);
}
