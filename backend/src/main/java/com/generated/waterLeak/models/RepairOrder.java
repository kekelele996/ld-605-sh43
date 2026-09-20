package com.generated.waterLeak.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "repair_order")
public class RepairOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "leak_report_id")
  private Long leakReportId;

  @Column(name = "crew_id")
  private Long crewId;

  @Column(name = "priority")
  private String priority;

  @Column(name = "status")
  private String status;

  @Column(name = "planned_start")
  private LocalDateTime plannedStart;

  @Column(name = "finished_at")
  private LocalDateTime finishedAt;

  @Column(name = "cost_amount")
  private BigDecimal costAmount;

  /** 派工日期：同一维修队同一天只能有一张未关闭维修单的唯一约束基于此列。 */
  @Column(name = "dispatch_date")
  private LocalDate dispatchDate;

  /** 最近一次验收失败等原因，持久化以保证刷新后仍可读。 */
  @Column(name = "block_reason")
  private String blockReason;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public Long getLeakReportId() { return leakReportId; }
  public void setLeakReportId(Long leakReportId) { this.leakReportId = leakReportId; }
  public Long getCrewId() { return crewId; }
  public void setCrewId(Long crewId) { this.crewId = crewId; }
  public String getPriority() { return priority; }
  public void setPriority(String priority) { this.priority = priority; }
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
  public LocalDateTime getPlannedStart() { return plannedStart; }
  public void setPlannedStart(LocalDateTime plannedStart) { this.plannedStart = plannedStart; }
  public LocalDateTime getFinishedAt() { return finishedAt; }
  public void setFinishedAt(LocalDateTime finishedAt) { this.finishedAt = finishedAt; }
  public BigDecimal getCostAmount() { return costAmount; }
  public void setCostAmount(BigDecimal costAmount) { this.costAmount = costAmount; }
  public LocalDate getDispatchDate() { return dispatchDate; }
  public void setDispatchDate(LocalDate dispatchDate) { this.dispatchDate = dispatchDate; }
  public String getBlockReason() { return blockReason; }
  public void setBlockReason(String blockReason) { this.blockReason = blockReason; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
