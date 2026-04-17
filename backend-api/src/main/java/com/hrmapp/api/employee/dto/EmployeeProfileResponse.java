package com.hrmapp.api.employee.dto;

import java.time.LocalDate;

public record EmployeeProfileResponse(
        Long employeeId,
        String employeeCode,
        String fullName,
        LocalDate dob,
        String gender,
        String workEmail,
        String personalPhone,
        Long orgUnitId,
        Long positionId,
        Long managerEmployeeId,
        LocalDate joinDate,
        String avatarUrl,
        String status
) {
}