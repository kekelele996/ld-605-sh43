package com.generated.waterLeak.services;

import com.generated.waterLeak.models.AuditLog;
import com.generated.waterLeak.repositories.AuditLogRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditLogService {
  private final AuditLogRepository repo;

  public AuditLogService(AuditLogRepository repo) { this.repo = repo; }

  /** 跟随当前事务记录操作日志；业务回滚时日志一并回滚，保证日志与状态一致。 */
  @Transactional(propagation = Propagation.REQUIRED)
  public void record(String actor, String action, String targetType, Long targetId, String detail) {
    AuditLog log = new AuditLog();
    log.actor = actor;
    log.action = action;
    log.targetType = targetType;
    log.targetId = targetId;
    log.detail = detail;
    log.createdAt = LocalDateTime.now();
    repo.save(log);
  }

  public List<AuditLog> listRecent() { return repo.findTop50ByOrderByIdDesc(); }
}
