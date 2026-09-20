package com.generated.waterLeak.services;

import com.generated.waterLeak.models.AuditLog;
import com.generated.waterLeak.repositories.AuditLogRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/** 操作日志：默认并入调用方事务；失败路径用 REQUIRES_NEW 独立提交。 */
@Service
public class AuditLogService {
  private final AuditLogRepository repo;

  public AuditLogService(AuditLogRepository repo) { this.repo = repo; }

  @Transactional(propagation = Propagation.REQUIRED)
  public void log(String actor, String action, String targetType, Object targetId) {
    save(actor, action, targetType, targetId);
  }

  /** 独立事务：业务事务回滚（验收失败、派工被阻塞）时，失败痕迹仍然落库。 */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void logIndependent(String actor, String action, String targetType, Object targetId) {
    save(actor, action, targetType, targetId);
  }

  private void save(String actor, String action, String targetType, Object targetId) {
    AuditLog log = new AuditLog();
    log.setActor(actor);
    log.setAction(action);
    log.setTargetType(targetType);
    log.setTargetId(targetId == null ? null : String.valueOf(targetId));
    log.setDetail(action);
    log.setCreatedAt(LocalDateTime.now());
    repo.save(log);
  }
}
