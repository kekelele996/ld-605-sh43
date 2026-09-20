package com.generated.waterLeak.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "material_usage")
public class MaterialUsage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "repair_order_id")
  private Long repairOrderId;

  @Column(name = "material_code")
  private String materialCode;

  @Column(name = "material_name")
  private String materialName;

  @Column(name = "quantity")
  private BigDecimal quantity;

  @Column(name = "unit")
  private String unit;

  @Column(name = "warehouse")
  private String warehouse;

  @Column(name = "usage_status")
  private String usageStatus;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public Long getRepairOrderId() { return repairOrderId; }
  public void setRepairOrderId(Long repairOrderId) { this.repairOrderId = repairOrderId; }
  public String getMaterialCode() { return materialCode; }
  public void setMaterialCode(String materialCode) { this.materialCode = materialCode; }
  public String getMaterialName() { return materialName; }
  public void setMaterialName(String materialName) { this.materialName = materialName; }
  public BigDecimal getQuantity() { return quantity; }
  public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
  public String getUnit() { return unit; }
  public void setUnit(String unit) { this.unit = unit; }
  public String getWarehouse() { return warehouse; }
  public void setWarehouse(String warehouse) { this.warehouse = warehouse; }
  public String getUsageStatus() { return usageStatus; }
  public void setUsageStatus(String usageStatus) { this.usageStatus = usageStatus; }
}
