package com.generated.waterLeak.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pipeline_segment")
public class PipelineSegment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "segment_code")
  private String segmentCode;

  @Column(name = "district")
  private String district;

  @Column(name = "material")
  private String material;

  @Column(name = "diameter")
  private String diameter;

  @Column(name = "install_year")
  private String installYear;

  @Column(name = "pressure_zone")
  private String pressureZone;

  @Column(name = "risk_level")
  private String riskLevel;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getSegmentCode() { return segmentCode; }
  public void setSegmentCode(String segmentCode) { this.segmentCode = segmentCode; }
  public String getDistrict() { return district; }
  public void setDistrict(String district) { this.district = district; }
  public String getMaterial() { return material; }
  public void setMaterial(String material) { this.material = material; }
  public String getDiameter() { return diameter; }
  public void setDiameter(String diameter) { this.diameter = diameter; }
  public String getInstallYear() { return installYear; }
  public void setInstallYear(String installYear) { this.installYear = installYear; }
  public String getPressureZone() { return pressureZone; }
  public void setPressureZone(String pressureZone) { this.pressureZone = pressureZone; }
  public String getRiskLevel() { return riskLevel; }
  public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
}
