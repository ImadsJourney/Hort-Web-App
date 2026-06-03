package com.schoolms.school_management.child;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChildRepository extends JpaRepository<Child, Long> {

  List<Child> findByHortGroupId(Long hortGroupId);
}
