package com.generated.waterLeak.services;

import com.generated.waterLeak.constants.ErrorCodes;
import com.generated.waterLeak.constants.ErrorMessages;
import com.generated.waterLeak.constants.LeakLevel;
import com.generated.waterLeak.constants.LogTemplates;
import com.generated.waterLeak.constants.RepairStatus;
import com.generated.waterLeak.constants.UsageStatus;
import com.generated.waterLeak.constants.VerifyStatus;
import com.generated.waterLeak.constructors.RepairOrderDtoFactory;
import com.generated.waterLeak.middlewares.BizException;
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
import com.generated.waterLeak.types.MaterialUsagePayload;
import com.generated.waterLeak.types.RepairOrderPayload;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
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

  public RepairOrderService(RepairOrderRepository repo, LeakReportRepository leakReportRepo,
      RepairCrewRepository crewRepo, MaterialStockRepository stockRepo,
      MaterialUsageRepository usageRepo, AuditLogService auditLogService) {
    this.repo = repo;
    this.leakReportRepo = leakReportRepo;
    this.crewRepo = crewRepo;
    this.stockRepo = stockRepo;
    this.usageRepo = usageRepo;
    this.auditLogService = auditLogService;
  }

  public List<Map<String, Object>> list() {
    Map<Long, RepairCrew> crews = crewRepo.findAll().stream()
        .collect(Collectors.toMap(c -> c.id, Function.identity()));
    Map<Long, LeakReport> reports = leakReportRepo.findAll().stream()
        .collect(Collectors.toMap(r -> r.id, Function.identity()));
    return repo.findAll().stream()
        .sorted((a, b) -> b.id.compareTo(a.id))
        .map(order -> {
          RepairCrew crew = crews.get(order.crewId);
          LeakReport report = reports.get(order.leakReportId);
          return RepairOrderDtoFactory.toResponse(order,
              crew == null ? null : crew.crewName,
              report == null ? null : report.leakLevel);
        })
        .toList();
  }

  /**
   * 派工。规则：
   * 1. BURST 级漏损未核实（非 VERIFIED）不能派工；
   * 2. 同一报告不允许重复开未关闭维修单；
   * 3. 同一维修队同一天只能有一张未关闭维修单 —— 先预检，再由数据库部分唯一索引兜底，
   *    并发派工时只有一单能提交成功，其余收到 CREW_DAY_CONFLICT。
   */
  @Transactional
  public Map<String, Object> dispatch(RepairOrderPayload payload) {
    if (payload == null || payload.leakReportId() == null || payload.crewId() == null) {
      throw BizException.badRequest(ErrorCodes.VALIDATION_FAILED, ErrorMessages.VALIDATION_FAILED);
    }
    LeakReport report = leakReportRepo.findById(payload.leakReportId())
        .orElseThrow(() -> BizException.notFound(ErrorCodes.REPORT_NOT_FOUND, ErrorMessages.REPORT_NOT_FOUND));
    RepairCrew crew = crewRepo.findById(payload.crewId())
        .orElseThrow(() -> BizException.badRequest(ErrorCodes.VALIDATION_FAILED, ErrorMessages.VALIDATION_FAILED));

    if (LeakLevel.BURST.name().equals(report.leakLevel) && !VerifyStatus.VERIFIED.name().equals(report.verifyStatus)) {
      throw BizException.conflict(ErrorCodes.BURST_UNVERIFIED, ErrorMessages.BURST_UNVERIFIED);
    }
    if (!repo.findByLeakReportIdAndStatusNot(report.id, RepairStatus.CLOSED.name()).isEmpty()) {
      throw BizException.conflict(ErrorCodes.ORDER_ALREADY_OPEN, ErrorMessages.ORDER_ALREADY_OPEN);
    }

    LocalDate workDate = payload.workDate() == null || payload.workDate().isBlank()
        ? LocalDate.now() : LocalDate.parse(payload.workDate().trim());
    if (!repo.findByCrewIdAndWorkDateAndStatusNot(crew.id, workDate, RepairStatus.CLOSED.name()).isEmpty()) {
      throw BizException.conflict(ErrorCodes.CREW_DAY_CONFLICT, ErrorMessages.CREW_DAY_CONFLICT);
    }

    RepairOrder order = new RepairOrder();
    order.leakReportId = report.id;
    order.crewId = crew.id;
    order.priority = payload.priority() == null || payload.priority().isBlank()
        ? report.leakLevel : payload.priority().trim().toUpperCase();
    order.status = RepairStatus.ASSIGNED.name();
    order.plannedStart = LocalDateTime.now();
    order.workDate = workDate;
    try {
      RepairOrder saved = repo.saveAndFlush(order);
      auditLogService.record("dispatcher", LogTemplates.CREATE, "RepairOrder", saved.id,
          String.format(LogTemplates.DISPATCH, saved.id, saved.leakReportId, saved.crewId, saved.workDate));
      return RepairOrderDtoFactory.toResponse(saved, crew.crewName, report.leakLevel);
    } catch (DataIntegrityViolationException ex) {
      // 并发派工撞上 (crew_id, work_date) 未关闭唯一索引：只成功一单。
      throw BizException.conflict(ErrorCodes.CREW_DAY_CONFLICT, ErrorMessages.CREW_DAY_CONFLICT);
    }
  }

  /**
   * 领料 + 验收：同一事务。
   * 先按行锁扣减库存并写入 PICKED 流水；验收未通过则抛异常整体回滚 —— 不扣料、不关单；
   * 验收通过才把流水转 CONSUMED、维修单转 CLOSED 并记录费用。
   */
  @Transactional
  public Map<String, Object> accept(Long orderId, MaterialUsagePayload payload) {
    RepairOrder order = repo.findByIdForUpdate(orderId)
        .orElseThrow(() -> BizException.notFound(ErrorCodes.ORDER_NOT_FOUND, ErrorMessages.ORDER_NOT_FOUND));
    if (RepairStatus.CLOSED.name().equals(order.status)) {
      throw BizException.conflict(ErrorCodes.ORDER_STATE_INVALID, ErrorMessages.ORDER_STATE_INVALID);
    }

    List<MaterialUsagePayload.MaterialLine> lines = payload == null || payload.materials() == null
        ? List.of() : payload.materials();
    for (MaterialUsagePayload.MaterialLine line : lines) {
      if (line.materialCode() == null || line.warehouse() == null || line.quantity() == null || line.quantity() <= 0) {
        throw BizException.badRequest(ErrorCodes.VALIDATION_FAILED, ErrorMessages.VALIDATION_FAILED);
      }
      MaterialStock stock = stockRepo.findByWarehouseAndMaterialCode(line.warehouse(), line.materialCode())
          .orElseThrow(() -> BizException.conflict(ErrorCodes.INSUFFICIENT_STOCK, ErrorMessages.INSUFFICIENT_STOCK));
      if (stock.quantity < line.quantity()) {
        throw BizException.conflict(ErrorCodes.INSUFFICIENT_STOCK, ErrorMessages.INSUFFICIENT_STOCK);
      }
      stock.quantity -= line.quantity();
      stockRepo.save(stock);

      MaterialUsage usage = new MaterialUsage();
      usage.repairOrderId = order.id;
      usage.materialCode = stock.materialCode;
      usage.materialName = stock.materialName;
      usage.quantity = line.quantity();
      usage.unit = stock.unit;
      usage.warehouse = stock.warehouse;
      usage.usageStatus = UsageStatus.PICKED.name();
      usageRepo.save(usage);
      auditLogService.record("crew", LogTemplates.CREATE, "MaterialUsage", usage.id,
          String.format(LogTemplates.PICKUP, order.id, usage.materialCode, usage.quantity));
    }

    boolean passed = payload != null && Boolean.TRUE.equals(payload.acceptancePassed());
    if (!passed) {
      // 验收失败：事务回滚，库存扣减与 PICKED 流水全部撤销，维修单保持未关闭。
      auditLogService.record("crew", LogTemplates.STATUS, "RepairOrder", order.id,
          String.format(LogTemplates.ACCEPT_FAILED, order.id));
      throw BizException.conflict(ErrorCodes.ACCEPTANCE_FAILED, ErrorMessages.ACCEPTANCE_FAILED);
    }

    usageRepo.findByRepairOrderIdOrderByIdAsc(order.id).forEach(usage -> {
      usage.usageStatus = UsageStatus.CONSUMED.name();
      usageRepo.save(usage);
    });
    order.status = RepairStatus.CLOSED.name();
    order.finishedAt = LocalDateTime.now();
    order.costAmount = payload.costAmount();
    RepairOrder saved = repo.save(order);
    auditLogService.record("crew", LogTemplates.STATUS, "RepairOrder", saved.id,
        String.format(LogTemplates.ACCEPT, saved.id, saved.costAmount));

    RepairCrew crew = crewRepo.findById(saved.crewId).orElse(null);
    LeakReport report = leakReportRepo.findById(saved.leakReportId).orElse(null);
    return RepairOrderDtoFactory.toResponse(saved,
        crew == null ? null : crew.crewName,
        report == null ? null : report.leakLevel);
  }
}
