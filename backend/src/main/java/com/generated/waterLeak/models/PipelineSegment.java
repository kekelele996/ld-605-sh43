package com.generated.waterLeak.models;

import jakarta.persistence.*;

@Entity
@Table(name = "pipeline_segment")
public class PipelineSegment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @Column(name = "segment_code", nullable = false)
  public String segmentCode;

  @Column(name = "district")
  public String district;

  @Column(name = "material")
  public String material;

  @Column(name = "diameter")
  public String diameter;

  @Column(name = "install_year")
  public Integer installYear;

  @Column(name = "pressure_zone")
  public String pressureZone;

  @Column(name = "risk_level", nullable = false)
  public String riskLevel;
}
