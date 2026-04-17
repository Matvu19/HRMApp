package com.hrmapp.api.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AttendanceCheckRequest(
        @NotNull Long employeeId,
        @NotNull Long assignmentId,
        @NotBlank String eventType,
        @NotNull LocalDateTime eventTimeDevice,
        BigDecimal geoLat,
        BigDecimal geoLng,
        String deviceId,
        @NotBlank String idempotencyKey
) {
}