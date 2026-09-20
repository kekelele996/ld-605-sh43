package com.generated.waterLeak.models;

import jakarta.persistence.*;

@Entity
@Table(name = "material_usage")
public class MaterialUsage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @Column(name = "repair_order_id", nullable = false)
  public Long repairOrderId;

  @Column(name = "material_code", nullable = false)
  public String materialCode;

  @Column(name = "material_name")
  public String materialName;

  @Column(name = "quantity", nullable = false)
  public Integer quantity;

  @Column(name = "unit")
  public String unit;

  @Column(name = "warehouse", nullable = false)
  public String warehouse;

  @Column(name = "usage_status", nullable = false)
  public String usageStatus;
}
