package com.generated.waterLeak.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "material_stock")
public class MaterialStock {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "material_code")
  private String materialCode;

  @Column(name = "material_name")
  private String materialName;

  @Column(name = "warehouse")
  private String warehouse;

  @Column(name = "quantity")
  private BigDecimal quantity;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getMaterialCode() { return materialCode; }
  public void setMaterialCode(String materialCode) { this.materialCode = materialCode; }
  public String getMaterialName() { return materialName; }
  public void setMaterialName(String materialName) { this.materialName = materialName; }
  public String getWarehouse() { return warehouse; }
  public void setWarehouse(String warehouse) { this.warehouse = warehouse; }
  public BigDecimal getQuantity() { return quantity; }
  public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
}
