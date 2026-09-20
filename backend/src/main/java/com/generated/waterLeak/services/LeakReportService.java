package com.generated.waterLeak.services;

import com.generated.waterLeak.constants.ErrorCodes;
import com.generated.waterLeak.constants.ErrorMessages;
import com.generated.waterLeak.constants.LeakLevel;
import com.generated.waterLeak.constants.LogTemplates;
import com.generated.waterLeak.constants.RepairStatus;
import com.generated.waterLeak.constants.VerifyStatus;
import com.generated.waterLeak.constructors.LeakReportDtoFactory;
import com.generated.waterLeak.constructors.VerificationQueueItem;
import com.generated.waterLeak.middlewares.BizException;
import com.generated.waterLeak.models.InspectionPoint;
import com.generated.waterLeak.models.LeakReport;
import com.generated.waterLeak.models.PipelineSegment;
import com.generated.waterLeak.models.RepairOrder;
import com.generated.waterLeak.repositories.InspectionPointRepository;
import com.generated.waterLeak.repositories.LeakReportRepository;
import com.generated.waterLeak.repositories.PipelineSegmentRepository;
import com.generated.waterLeak.repositories.RepairOrderRepository;
import com.generated.waterLeak.types.LeakReportPayload;
import com.generated.waterLeak.types.VerifyPayload;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LeakReportService {
  private final LeakReportRepository repo;
  private final InspectionPointRepository pointRepo;
  private final PipelineSegmentRepository segmentRepo;
  private final RepairOrderRepository orderRepo;
  private final AuditLogService auditLogService;

  public LeakReportService(LeakReportRepository repo, InspectionPointRepository pointRepo,
      PipelineSegmentRepository segmentRepo, RepairOrderRepository orderRepo, AuditLogService auditLogService) {
    this.repo = repo;
    this.pointRepo = pointRepo;
    this.segmentRepo = segmentRepo;
    this.orderRepo = orderRepo;
    this.auditLogService = auditLogService;
  }

  public List<Map<String, Object>> list() {
    return repo.findAll().stream().map(LeakReportDtoFactory::toResponse).toList();
  }

  /**
   * 漏损核查队列：漏损级别 BURST→TRACE，管段风险 EXTREME→LOW，同分看巡检超期天数（超期多者优先）。
   * 每行携带 dispatchable / blockedReason，刷新后由服务端按最新状态重算。
   */
  public List<Map<String, Object>> verificationQueue() {
    Map<Long, InspectionPoint> points = pointRepo.findAll().stream()
        .collect(Collectors.toMap(p -> p.id, Function.identity()));
    Map<Long, PipelineSegment> segments = segmentRepo.findAll().stream()
        .collect(Collectors.toMap(s -> s.id, Function.identity()));
    Map<Long, Long> openOrderByReport = orderRepo.findAll().stream()
        .filter(o -> !RepairStatus.CLOSED.name().equals(o.status))
        .collect(Collectors.toMap(o -> o.leakReportId, o -> o.id, (a, b) -> a));

    return repo.findAll().stream()
        .map(report -> {
          InspectionPoint point = points.get(report.pointId);
          PipelineSegment segment = point == null ? null : segments.get(point.pipelineSegmentId);
          return VerificationQueueItem.of(report, point, segment, openOrderByReport.get(report.id));
        })
        .sorted(Comparator.comparingInt(VerificationQueueItem::leakRank).reversed()
            .thenComparing(Comparator.comparingInt(VerificationQueueItem::riskRank).reversed())
            .thenComparing(Comparator.comparingLong(VerificationQueueItem::overdueDays).reversed())
            .thenComparing(VerificationQueueItem::reportId))
        .map(LeakReportDtoFactory::toQueueResponse)
        .toList();
  }

  /** 漏损上报：默认待核实（PENDING）。 */
  @Transactional
  public Map<String, Object> report(LeakReportPayload payload) {
    if (payload == null || payload.pointId() == null || payload.leakLevel() == null) {
      throw BizException.badRequest(ErrorCodes.VALIDATION_FAILED, ErrorMessages.VALIDATION_FAILED);
    }
    InspectionPoint point = pointRepo.findById(payload.pointId())
        .orElseThrow(() -> BizException.notFound(ErrorCodes.POINT_NOT_FOUND, ErrorMessages.POINT_NOT_FOUND));
    String level = parseEnum(LeakLevel.class, payload.leakLevel());
    String reporterType = payload.reporterType() == null || payload.reporterType().isBlank()
        ? "RESIDENT" : payload.reporterType().trim().toUpperCase();

    LeakReport report = new LeakReport();
    report.pointId = point.id;
    report.leakLevel = level;
    report.reporterType = reporterType;
    report.description = payload.description();
    report.photoUrl = payload.photoUrl();
    report.reportedAt = LocalDateTime.now();
    report.verifyStatus = VerifyStatus.PENDING.name();
    LeakReport saved = repo.save(report);
    auditLogService.record("dispatcher", LogTemplates.CREATE, "LeakReport", saved.id,
        String.format(LogTemplates.LEAK_REPORT, saved.id, saved.pointId, saved.leakLevel));
    return LeakReportDtoFactory.toResponse(saved);
  }

  /** 漏损核实：只允许 VERIFIED / REJECTED。 */
  @Transactional
  public Map<String, Object> verify(Long id, VerifyPayload payload) {
    LeakReport report = repo.findById(id)
        .orElseThrow(() -> BizException.notFound(ErrorCodes.REPORT_NOT_FOUND, ErrorMessages.REPORT_NOT_FOUND));
    String status = payload == null ? null : parseEnum(VerifyStatus.class, payload.verifyStatus());
    if (VerifyStatus.PENDING.name().equals(status)) {
      throw BizException.badRequest(ErrorCodes.VALIDATION_FAILED, ErrorMessages.VALIDATION_FAILED);
    }
    report.verifyStatus = status;
    LeakReport saved = repo.save(report);
    auditLogService.record("dispatcher", LogTemplates.STATUS, "LeakReport", saved.id,
        String.format(LogTemplates.LEAK_VERIFY, saved.id, saved.verifyStatus));
    return LeakReportDtoFactory.toResponse(saved);
  }

  static <E extends Enum<E>> String parseEnum(Class<E> type, String value) {
    if (value == null || value.isBlank()) {
      throw BizException.badRequest(ErrorCodes.VALIDATION_FAILED, ErrorMessages.VALIDATION_FAILED);
    }
    try {
      return Enum.valueOf(type, value.trim().toUpperCase()).name();
    } catch (IllegalArgumentException ex) {
      throw BizException.badRequest(ErrorCodes.VALIDATION_FAILED, ErrorMessages.VALIDATION_FAILED);
    }
  }
}
