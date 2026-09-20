package com.generated.waterLeak.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "leak_report")
public class LeakReport {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "reporter_type")
  private String reporterType;

  @Column(name = "point_id")
  private Long pointId;

  @Column(name = "leak_level")
  private String leakLevel;

  @Column(name = "description")
  private String description;

  @Column(name = "reported_at")
  private LocalDateTime reportedAt;

  @Column(name = "verify_status")
  private String verifyStatus;

  @Column(name = "photo_url")
  private String photoUrl;

  /** 最近一次被阻塞的业务原因（如 BURST 未核实派工、维修队当日冲突），持久化以保证刷新后仍可读。 */
  @Column(name = "block_reason")
  private String blockReason;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getReporterType() { return reporterType; }
  public void setReporterType(String reporterType) { this.reporterType = reporterType; }
  public Long getPointId() { return pointId; }
  public void setPointId(Long pointId) { this.pointId = pointId; }
  public String getLeakLevel() { return leakLevel; }
  public void setLeakLevel(String leakLevel) { this.leakLevel = leakLevel; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public LocalDateTime getReportedAt() { return reportedAt; }
  public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }
  public String getVerifyStatus() { return verifyStatus; }
  public void setVerifyStatus(String verifyStatus) { this.verifyStatus = verifyStatus; }
  public String getPhotoUrl() { return photoUrl; }
  public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
  public String getBlockReason() { return blockReason; }
  public void setBlockReason(String blockReason) { this.blockReason = blockReason; }
}
