package com.generated.waterLeak.models;

import jakarta.persistence.*;

@Entity
@Table(name = "material_stock", uniqueConstraints = @UniqueConstraint(columnNames = {"warehouse", "material_code"}))
public class MaterialStock {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @Column(name = "material_code", nullable = false)
  public String materialCode;

  @Column(name = "material_name", nullable = false)
  public String materialName;

  @Column(name = "unit")
  public String unit;

  @Column(name = "warehouse", nullable = false)
  public String warehouse;

  @Column(name = "quantity", nullable = false)
  public Integer quantity;
}
