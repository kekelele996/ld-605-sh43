package com.generated.waterLeak.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "inspection_point")
public class InspectionPoint {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "pipeline_segment_id")
  private Long pipelineSegmentId;

  @Column(name = "point_code")
  private String pointCode;

  @Column(name = "point_type")
  private String pointType;

  @Column(name = "address_desc")
  private String addressDesc;

  /** 巡检周期，形如 7d / 15d / 30d，超期判定依赖该值。 */
  @Column(name = "check_frequency")
  private String checkFrequency;

  @Column(name = "last_checked_at")
  private LocalDateTime lastCheckedAt;

  @Column(name = "status")
  private String status;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public Long getPipelineSegmentId() { return pipelineSegmentId; }
  public void setPipelineSegmentId(Long pipelineSegmentId) { this.pipelineSegmentId = pipelineSegmentId; }
  public String getPointCode() { return pointCode; }
  public void setPointCode(String pointCode) { this.pointCode = pointCode; }
  public String getPointType() { return pointType; }
  public void setPointType(String pointType) { this.pointType = pointType; }
  public String getAddressDesc() { return addressDesc; }
  public void setAddressDesc(String addressDesc) { this.addressDesc = addressDesc; }
  public String getCheckFrequency() { return checkFrequency; }
  public void setCheckFrequency(String checkFrequency) { this.checkFrequency = checkFrequency; }
  public LocalDateTime getLastCheckedAt() { return lastCheckedAt; }
  public void setLastCheckedAt(LocalDateTime lastCheckedAt) { this.lastCheckedAt = lastCheckedAt; }
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
}
