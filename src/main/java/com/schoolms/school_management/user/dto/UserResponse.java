package com.schoolms.school_management.user.dto;

/**
 * UserResponse
 */
public record UserResponse(
    Long id,
    String firstName,
    String lastName,
    String email) {
}
