package com.generated.waterLeak.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "repair_order")
public class RepairOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @Column(name = "leak_report_id", nullable = false)
  public Long leakReportId;

  @Column(name = "crew_id", nullable = false)
  public Long crewId;

  @Column(name = "priority")
  public String priority;

  @Column(name = "status", nullable = false)
  public String status;

  @Column(name = "planned_start")
  public LocalDateTime plannedStart;

  /** 派工日期：同一维修队同一天只允许一张未关闭维修单（部分唯一索引保证并发安全）。 */
  @Column(name = "work_date", nullable = false)
  public LocalDate workDate;

  @Column(name = "finished_at")
  public LocalDateTime finishedAt;

  @Column(name = "cost_amount")
  public BigDecimal costAmount;
}
