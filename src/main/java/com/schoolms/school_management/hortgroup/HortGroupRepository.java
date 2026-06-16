package com.schoolms.school_management.hortgroup;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * HortGroupRepository
 */
public interface HortGroupRepository extends JpaRepository<HortGroup, Long> {

  List<HortGroup> findAllByOwnerId(Long ownerId);

  Optional<HortGroup> findByIdAndOwnerId(Long groupId, Long ownerId);

}
