package com.schoolms.school_management.child;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildRepository extends JpaRepository<Child, Long> {

  List<Child> findAllByHortGroupOwnerIdOrderByLastNameAscFirstNameAsc(
      Long ownerId);

  List<Child> findByHortGroupIdAndHortGroupOwnerIdOrderByLastNameAscFirstNameAsc(
      Long groupId,
      Long ownerId);

  Optional<Child> findByIdAndHortGroupOwnerId(
      Long childId,
      Long ownerId);
}
