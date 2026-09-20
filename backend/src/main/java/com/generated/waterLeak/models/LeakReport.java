package com.generated.waterLeak.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "leak_report")
public class LeakReport {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @Column(name = "reporter_type", nullable = false)
  public String reporterType;

  @Column(name = "point_id", nullable = false)
  public Long pointId;

  @Column(name = "leak_level", nullable = false)
  public String leakLevel;

  @Column(name = "description")
  public String description;

  @Column(name = "reported_at", nullable = false)
  public LocalDateTime reportedAt;

  @Column(name = "verify_status", nullable = false)
  public String verifyStatus;

  @Column(name = "photo_url")
  public String photoUrl;
}
