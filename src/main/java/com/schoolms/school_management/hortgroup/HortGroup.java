package com.schoolms.school_management.hortgroup;

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

}
