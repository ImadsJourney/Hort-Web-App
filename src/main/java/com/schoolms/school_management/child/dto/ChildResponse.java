package com.schoolms.school_management.child.dto;

import com.schoolms.school_management.child.AttendanceStatus;

public record ChildResponse(
    Long id,
    String firstName,
    String lastName,
    AttendanceStatus attendanceStatus,
    String notes,
    Long hortGroupId,
    String hortGroupName) {
}
