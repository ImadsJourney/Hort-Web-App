package com.schoolms.school_management.hortgroup.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateHortGroupRequest(
    @NotBlank(message = "Group name is required") String name,

    String gradeLevel,

    String supervisorName) {
}
