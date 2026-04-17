package com.hrmapp.api.attendance.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AttendanceEventResponse(
        Long eventId,
        Long employeeId,
        Long assignmentId,
        String eventType,
        LocalDateTime eventTimeDevice,
        LocalDateTime eventTimeServer,
        BigDecimal geoLat,
        BigDecimal geoLng,
        String status
) {
}