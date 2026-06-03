package com.schoolms.school_management.child.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateChildRequest(
    @NotBlank(message = "First name is required") String firstName,

    @NotBlank(message = "Last name is required") String lastName,

    String notes,

    @NotNull(message = "Group id is required") Long hortGroupId) {
}
