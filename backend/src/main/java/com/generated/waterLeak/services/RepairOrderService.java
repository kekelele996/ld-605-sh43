package com.generated.waterLeak.services;

import com.generated.waterLeak.constants.ErrorCodes;
import com.generated.waterLeak.constants.ErrorMessages;
import com.generated.waterLeak.constants.LeakLevel;
import com.generated.waterLeak.constants.LogTemplates;
import com.generated.waterLeak.constants.RepairStatus;
import com.generated.waterLeak.constants.UsageStatus;
import com.generated.waterLeak.constants.VerifyStatus;
import com.generated.waterLeak.constructors.RepairCrewDtoFactory;
import com.generated.waterLeak.constructors.RepairOrderDtoFactory;
import com.generated.waterLeak.models.LeakReport;
import com.generated.waterLeak.models.MaterialStock;
import com.generated.waterLeak.models.MaterialUsage;
import com.generated.waterLeak.models.RepairCrew;
import com.generated.waterLeak.models.RepairOrder;
import com.generated.waterLeak.repositories.LeakReportRepository;
import com.generated.waterLeak.repositories.MaterialStockRepository;
import com.generated.waterLeak.repositories.MaterialUsageRepository;
import com.generated.waterLeak.repositories.RepairCrewRepository;
import com.generated.waterLeak.repositories.RepairOrderRepository;
import com.generated.waterLeak.types.BizException;
import com.generated.waterLeak.types.MaterialUsagePayload;
import com.generated.waterLeak.types.RepairOrderAcceptPayload;
import com.generated.waterLeak.types.RepairOrderPayload;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RepairOrderService {
  private final RepairOrderRepository repo;
  private final LeakReportRepository leakReportRepo;
  private final RepairCrewRepository crewRepo;
  private final MaterialStockRepository stockRepo;
  private final MaterialUsageRepository usageRepo;
  private final AuditLogService auditLogService;
  private final BlockReasonService blockReasonService;

  public RepairOrderService(RepairOrderRepository repo, LeakReportRepository leakReportRepo,
                            RepairCrewRepository crewRepo, MaterialStockRepository stockRepo,
                            MaterialUsageRepository usageRepo, AuditLogService auditLogService,
                            BlockReasonService blockReasonService) {
    this.repo = repo;
    this.leakReportRepo = leakReportRepo;
    this.crewRepo = crewRepo;
    this.stockRepo = stockRepo;
    this.usageRepo = usageRepo;
    this.auditLogService = auditLogService;
    this.blockReasonService = blockReasonService;
  }

  @Transactional(readOnly = true)
  public List<Map<String, Object>> list() {
    return repo.findAllJoined().stream().map(RepairOrderDtoFactory::toResponse).toList();
  }

  @Transactional(readOnly = true)
  public List<Map<String, Object>> listCrews() {
    return crewRepo.findAll().stream().map(RepairCrewDtoFactory::toResponse).toList();
  }

  /**
   * 派工：
   * 1. BURST 级漏损未核实不能派工；
   * 2. 同一维修队同一天只能有一张未关闭维修单——悲观锁维修队行串行化并发派工，
   *    数据库 (crew_id, dispatch_date) 未关闭唯一索引兜底，并发派工只成功一单。
   */
  @Transactional
  public Map<String, Object> dispatch(RepairOrderPayload payload) {
    LeakReport report = leakReportRepo.findById(payload.leakReportId())
      .orElseThrow(() -> new BizException(ErrorCodes.NOT_FOUND, "漏损报告不存在: " + payload.leakReportId()));

    if (LeakLevel.BURST.name().equals(report.getLeakLevel()) && !VerifyStatus.VERIFIED.name().equals(report.getVerifyStatus())) {
      blockReasonService.markReportBlocked(report.getId(), ErrorMessages.BURST_UNVERIFIED);
      throw new BizException(ErrorCodes.BURST_UNVERIFIED, ErrorMessages.BURST_UNVERIFIED);
    }

    LocalDate dispatchDate = payload.dispatchDate() != null ? payload.dispatchDate() : LocalDate.now();
    RepairCrew crew = crewRepo.findByIdForUpdate(payload.crewId())
      .orElseThrow(() -> new BizException(ErrorCodes.NOT_FOUND, "维修队不存在: " + payload.crewId()));

    long openCount = repo.countByCrewIdAndDispatchDateAndStatusNot(crew.getId(), dispatchDate, RepairStatus.CLOSED.name());
    if (openCount > 0) {
      auditLogService.logIndependent("dispatcher", String.format(LogTemplates.ORDER_DISPATCH_CONFLICT, crew.getId(), dispatchDate), "RepairOrder", null);
      blockReasonService.markReportBlocked(report.getId(), ErrorMessages.CREW_DAY_CONFLICT);
      throw new BizException(ErrorCodes.CREW_DAY_CONFLICT, ErrorMessages.CREW_DAY_CONFLICT);
    }

    RepairOrder order = new RepairOrder();
    order.setLeakReportId(report.getId());
    order.setCrewId(crew.getId());
    order.setPriority(payload.priority() == null || payload.priority().isBlank() ? "P2" : payload.priority());
    order.setStatus(RepairStatus.ASSIGNED.name());
    order.setPlannedStart(dispatchDate.atTime(8, 0));
    order.setDispatchDate(dispatchDate);
    order.setCreatedAt(LocalDateTime.now());
    repo.save(order);

    report.setBlockReason(null);
    leakReportRepo.save(report);

    auditLogService.log("dispatcher", String.format(LogTemplates.ORDER_DISPATCH, order.getId(), report.getId(), crew.getId(), dispatchDate), "RepairOrder", order.getId());
    return Map.of("id", order.getId(), "status", order.getStatus(), "dispatch_date", dispatchDate.toString());
  }

  /**
   * 领料 + 验收：写在同一事务。
   * 验收未通过或库存不足 → 整体回滚，不得扣料或关单；
   * 阻塞原因由 BlockReasonService 在独立事务落库，刷新后仍可读。
   */
  @Transactional
  public Map<String, Object> accept(Long orderId, RepairOrderAcceptPayload payload) {
    RepairOrder order = repo.findById(orderId)
      .orElseThrow(() -> new BizException(ErrorCodes.NOT_FOUND, "维修单不存在: " + orderId));

    if (RepairStatus.CLOSED.name().equals(order.getStatus())) {
      throw new BizException(ErrorCodes.ORDER_STATE_INVALID, "维修单已关闭，不能重复验收");
    }

    if (!Boolean.TRUE.equals(payload.acceptancePassed())) {
      String reason = ErrorMessages.ACCEPTANCE_FAILED + (payload.note() == null || payload.note().isBlank() ? "" : "：" + payload.note());
      blockReasonService.markOrderBlocked(order.getId(), reason);
      throw new BizException(ErrorCodes.ACCEPTANCE_FAILED, reason);
    }

    List<MaterialUsagePayload> items = payload.items() == null ? List.of() : payload.items();
    try {
      for (MaterialUsagePayload item : items) {
        issueMaterial(order.getId(), item);
      }
    } catch (BizException e) {
      blockReasonService.markOrderBlocked(order.getId(), e.getMessage());
      throw e;
    }

    order.setStatus(RepairStatus.CLOSED.name());
    order.setFinishedAt(LocalDateTime.now());
    order.setCostAmount(payload.costAmount());
    order.setBlockReason(null);
    repo.save(order);

    auditLogService.log("acceptor", String.format(LogTemplates.ORDER_ACCEPT, order.getId(), String.valueOf(payload.costAmount())), "RepairOrder", order.getId());
    return Map.of("id", order.getId(), "status", order.getStatus());
  }

  /** 单条领料：锁库存行、扣减库存、写材料流水，全部在当前事务内。 */
  private void issueMaterial(Long orderId, MaterialUsagePayload item) {
    MaterialStock stock = stockRepo.findByCodeAndWarehouseForUpdate(item.materialCode(), item.warehouse())
      .orElseThrow(() -> new BizException(ErrorCodes.STOCK_INSUFFICIENT,
        ErrorMessages.STOCK_INSUFFICIENT + ": " + item.materialCode() + "@" + item.warehouse()));
    if (stock.getQuantity().compareTo(item.quantity()) < 0) {
      throw new BizException(ErrorCodes.STOCK_INSUFFICIENT,
        ErrorMessages.STOCK_INSUFFICIENT + ": " + item.materialCode() + " 现存 " + stock.getQuantity() + " 需 " + item.quantity());
    }
    stock.setQuantity(stock.getQuantity().subtract(item.quantity()));
    stockRepo.save(stock);

    MaterialUsage usage = new MaterialUsage();
    usage.setRepairOrderId(orderId);
    usage.setMaterialCode(item.materialCode());
    usage.setMaterialName(item.materialName());
    usage.setQuantity(item.quantity());
    usage.setUnit(item.unit());
    usage.setWarehouse(item.warehouse());
    usage.setUsageStatus(UsageStatus.ISSUED.name());
    usageRepo.save(usage);

    auditLogService.log("storekeeper", String.format(LogTemplates.MATERIAL_ISSUE, orderId, item.materialCode(), item.quantity(), item.warehouse()), "MaterialUsage", usage.getId());
    auditLogService.log("storekeeper", String.format(LogTemplates.MATERIAL_STOCK_DEDUCT, item.materialCode(), item.warehouse(), item.quantity()), "MaterialStock", stock.getId());
  }
}
