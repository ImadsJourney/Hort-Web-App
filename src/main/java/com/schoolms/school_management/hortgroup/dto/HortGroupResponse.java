package com.schoolms.school_management.hortgroup.dto;

public record HortGroupResponse(
    Long id,
    String name,
    String gradeLevel,
    String supervisorName) {
}
