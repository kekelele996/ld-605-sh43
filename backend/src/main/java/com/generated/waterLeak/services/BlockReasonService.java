package com.generated.waterLeak.services;

import com.generated.waterLeak.constants.LogTemplates;
import com.generated.waterLeak.repositories.LeakReportRepository;
import com.generated.waterLeak.repositories.RepairOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 阻塞原因落库：独立事务提交，保证业务事务回滚后，
 * 页面刷新仍能读到状态与阻塞原因。
 */
@Service
public class BlockReasonService {
  private final LeakReportRepository leakReportRepo;
  private final RepairOrderRepository repairOrderRepo;
  private final AuditLogService auditLogService;

  public BlockReasonService(LeakReportRepository leakReportRepo, RepairOrderRepository repairOrderRepo, AuditLogService auditLogService) {
    this.leakReportRepo = leakReportRepo;
    this.repairOrderRepo = repairOrderRepo;
    this.auditLogService = auditLogService;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void markReportBlocked(Long reportId, String reason) {
    leakReportRepo.findById(reportId).ifPresent(report -> {
      report.setBlockReason(reason);
      leakReportRepo.save(report);
    });
    auditLogService.logIndependent("dispatcher", String.format(LogTemplates.LEAK_BLOCK, reportId, reason), "LeakReport", reportId);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void markOrderBlocked(Long orderId, String reason) {
    repairOrderRepo.findById(orderId).ifPresent(order -> {
      order.setBlockReason(reason);
      repairOrderRepo.save(order);
    });
    auditLogService.logIndependent("acceptor", String.format(LogTemplates.ORDER_ACCEPT_FAILED, orderId, reason), "RepairOrder", orderId);
  }
}
