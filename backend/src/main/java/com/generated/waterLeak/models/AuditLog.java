package com.generated.waterLeak.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
public class AuditLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "actor")
  private String actor;

  @Column(name = "action")
  private String action;

  @Column(name = "target_type")
  private String targetType;

  @Column(name = "target_id")
  private String targetId;

  @Column(name = "detail")
  private String detail;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getActor() { return actor; }
  public void setActor(String actor) { this.actor = actor; }
  public String getAction() { return action; }
  public void setAction(String action) { this.action = action; }
  public String getTargetType() { return targetType; }
  public void setTargetType(String targetType) { this.targetType = targetType; }
  public String getTargetId() { return targetId; }
  public void setTargetId(String targetId) { this.targetId = targetId; }
  public String getDetail() { return detail; }
  public void setDetail(String detail) { this.detail = detail; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
