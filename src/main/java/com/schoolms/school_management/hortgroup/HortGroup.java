package com.schoolms.school_management.hortgroup;

import com.schoolms.school_management.user.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hort_groups")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HortGroup {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  private String gradeLevel;

  private String supervisorName;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "owner_id", nullable = false)
  private User owner;

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }

  public String getGradeLevel() {
    return gradeLevel;
  }

  public String getSupervisorName() {
    return supervisorName;
  }

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setGradeLevel(String gradeLevel) {
    this.gradeLevel = gradeLevel;
  }

  public void setSupervisorName(String supervisorName) {
    this.supervisorName = supervisorName;
  }

}
