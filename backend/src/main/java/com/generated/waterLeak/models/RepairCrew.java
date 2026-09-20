package com.generated.waterLeak.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "repair_crew")
public class RepairCrew {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "crew_code")
  private String crewCode;

  @Column(name = "crew_name")
  private String crewName;

  @Column(name = "contact_phone")
  private String contactPhone;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getCrewCode() { return crewCode; }
  public void setCrewCode(String crewCode) { this.crewCode = crewCode; }
  public String getCrewName() { return crewName; }
  public void setCrewName(String crewName) { this.crewName = crewName; }
  public String getContactPhone() { return contactPhone; }
  public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
}
