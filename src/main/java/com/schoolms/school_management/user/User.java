package com.schoolms.school_management.user;

import jakarta.persistence.*;
import lombok.*;

/**
 * User
 */

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  /**
   * Aus irgendeinem Grund erkennt mein LSP nicht das ich @Setter benutze und
   * deshalb werden die Setter mir nicht angezeigt in UserService.java
   * Sprich ich musste sie doch jetzt aufschreiben ;;;;)
   *
   */

  public void setId(Long id) {
    this.id = id;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
