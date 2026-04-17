package com.hrmapp.api.leave.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LeaveRequestCreateRequest(
        @NotNull Long employeeId,
        @NotNull Long leaveTypeId,
        @NotNull LocalDate dateFrom,
        @NotNull LocalDate dateTo,
        @NotNull BigDecimal durationDays,
        String reasonText
) {
}