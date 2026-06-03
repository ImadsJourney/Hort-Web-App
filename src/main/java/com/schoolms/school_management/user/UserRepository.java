package com.schoolms.school_management.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * UserRepository
 *
 * Basically it is there to not write SQL manually, so I just use methods like
 * findAll(), save() etc
 * The whole point of this is to communicate to the Database properly and more
 * easily
 * 
 */
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

}
