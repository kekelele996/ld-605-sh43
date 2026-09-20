package com.generated.waterLeak.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
public class AuditLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @Column(name = "actor")
  public String actor;

  @Column(name = "action", nullable = false)
  public String action;

  @Column(name = "target_type")
  public String targetType;

  @Column(name = "target_id")
  public Long targetId;

  @Column(name = "detail")
  public String detail;

  @Column(name = "created_at", nullable = false)
  public LocalDateTime createdAt;
}
