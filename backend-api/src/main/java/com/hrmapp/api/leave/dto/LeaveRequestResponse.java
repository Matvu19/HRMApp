package com.hrmapp.api.leave.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LeaveRequestResponse(
        Long leaveRequestId,
        Long employeeId,
        Long leaveTypeId,
        Long approvalFlowId,
        LocalDate dateFrom,
        LocalDate dateTo,
        BigDecimal durationDays,
        String reasonText,
        String status
) {
}