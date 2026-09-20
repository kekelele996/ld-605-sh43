package com.generated.waterLeak.repositories;

import com.generated.waterLeak.models.MaterialStock;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialStockRepository extends JpaRepository<MaterialStock, Long> {

  /** 领料扣减库存时加行级写锁，与验收同事务提交或回滚。 */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<MaterialStock> findByWarehouseAndMaterialCode(String warehouse, String materialCode);
}
