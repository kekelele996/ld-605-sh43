package com.generated.waterLeak.models;

import jakarta.persistence.*;

@Entity
@Table(name = "repair_crew")
public class RepairCrew {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @Column(name = "crew_code", nullable = false, unique = true)
  public String crewCode;

  @Column(name = "crew_name", nullable = false)
  public String crewName;

  @Column(name = "status", nullable = false)
  public String status;
}
