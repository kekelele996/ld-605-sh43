package com.generated.waterLeak.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inspection_point")
public class InspectionPoint {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @Column(name = "pipeline_segment_id", nullable = false)
  public Long pipelineSegmentId;

  @Column(name = "point_code", nullable = false)
  public String pointCode;

  @Column(name = "point_type")
  public String pointType;

  @Column(name = "address_desc")
  public String addressDesc;

  @Column(name = "check_frequency", nullable = false)
  public String checkFrequency;

  @Column(name = "last_checked_at")
  public LocalDateTime lastCheckedAt;

  @Column(name = "status")
  public String status;
}
