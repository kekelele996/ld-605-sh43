package com.generated.waterLeak.services;

import com.generated.waterLeak.constants.ErrorCodes;
import com.generated.waterLeak.constants.ErrorMessages;
import com.generated.waterLeak.constants.LeakLevel;
import com.generated.waterLeak.constants.LogTemplates;
import com.generated.waterLeak.constants.RiskLevel;
import com.generated.waterLeak.constants.VerifyStatus;
import com.generated.waterLeak.constructors.LeakReportDtoFactory;
import com.generated.waterLeak.models.LeakReport;
import com.generated.waterLeak.repositories.InspectionPointRepository;
import com.generated.waterLeak.repositories.LeakReportRepository;
import com.generated.waterLeak.types.BizException;
import com.generated.waterLeak.types.LeakReportPayload;
import com.generated.waterLeak.types.LeakReportView;
import com.generated.waterLeak.utils.Formatters;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LeakReportService {
  private final LeakReportRepository repo;
  private final InspectionPointRepository pointRepo;
  private final AuditLogService auditLogService;

  public LeakReportService(LeakReportRepository repo, InspectionPointRepository pointRepo, AuditLogService auditLogService) {
    this.repo = repo;
    this.pointRepo = pointRepo;
    this.auditLogService = auditLogService;
  }

  /**
   * 漏损核查列表：按漏损等级 BURST→TRACE、管段风险从高到低排序，同分看巡检超期。
   */
  @Transactional(readOnly = true)
  public List<Map<String, Object>> list() {
    LocalDateTime now = LocalDateTime.now();
    return repo.findAllJoined().stream()
      .sorted(Comparator
        .comparingInt((LeakReportView v) -> LeakLevel.rank(v.leakLevel()))
        .thenComparingInt(v -> RiskLevel.rank(v.riskLevel()))
        .thenComparing(Comparator.comparingLong((LeakReportView v) -> overdueDays(v, now)).reversed())
        .thenComparing(LeakReportView::id))
      .map(v -> LeakReportDtoFactory.toResponse(v, overdueDays(v, now), effectiveBlockReason(v)))
      .toList();
  }

  /** 上报：居民/巡检员上报漏损，初始为待核实。 */
  @Transactional
  public Map<String, Object> create(LeakReportPayload payload) {
    var point = pointRepo.findById(payload.pointId())
      .orElseThrow(() -> new BizException(ErrorCodes.NOT_FOUND, "巡检点不存在: " + payload.pointId()));
    try {
      LeakLevel.valueOf(payload.leakLevel());
    } catch (IllegalArgumentException e) {
      throw new BizException(ErrorCodes.VALIDATION_FAILED, "非法漏损等级: " + payload.leakLevel());
    }
    LeakReport report = new LeakReport();
    report.setReporterType(payload.reporterType());
    report.setPointId(point.getId());
    report.setLeakLevel(payload.leakLevel());
    report.setDescription(payload.description());
    report.setReportedAt(LocalDateTime.now());
    report.setVerifyStatus(VerifyStatus.PENDING.name());
    report.setPhotoUrl(payload.photoUrl());
    repo.save(report);
    auditLogService.log("reporter", String.format(LogTemplates.LEAK_CREATE, report.getId(), report.getLeakLevel(), point.getId()), "LeakReport", report.getId());
    return Map.of("id", report.getId(), "verify_status", report.getVerifyStatus());
  }

  /** 核实：VERIFIED 通过 / REJECTED 误报驳回；核实后清除历史阻塞原因。 */
  @Transactional
  public Map<String, Object> verify(Long id, String result) {
    LeakReport report = repo.findById(id)
      .orElseThrow(() -> new BizException(ErrorCodes.NOT_FOUND, ErrorMessages.NOT_FOUND + ": leak_report " + id));
    if (!VerifyStatus.VERIFIED.name().equals(result) && !VerifyStatus.REJECTED.name().equals(result)) {
      throw new BizException(ErrorCodes.VALIDATION_FAILED, "核实结果只能是 VERIFIED 或 REJECTED");
    }
    report.setVerifyStatus(result);
    report.setBlockReason(null);
    repo.save(report);
    auditLogService.log("verifier", String.format(LogTemplates.LEAK_VERIFY, id, result), "LeakReport", id);
    return Map.of("id", id, "verify_status", result);
  }

  private long overdueDays(LeakReportView v, LocalDateTime now) {
    return Formatters.overdueDays(v.lastCheckedAt(), v.checkFrequency(), now);
  }

  /** 持久化的阻塞原因优先；BURST 未核实派工阻塞为实时推导，保证刷新后仍可读。 */
  private String effectiveBlockReason(LeakReportView v) {
    if (v.blockReason() != null && !v.blockReason().isBlank()) return v.blockReason();
    if (LeakLevel.BURST.name().equals(v.leakLevel()) && !VerifyStatus.VERIFIED.name().equals(v.verifyStatus())) {
      return ErrorMessages.BURST_UNVERIFIED;
    }
    return null;
  }
}
