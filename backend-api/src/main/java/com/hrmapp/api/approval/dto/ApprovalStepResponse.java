package com.hrmapp.api.approval.dto;

import java.time.LocalDateTime;

public record ApprovalStepResponse(
        Long approvalStepId,
        Long approvalFlowId,
        Integer stepNo,
        Long approverEmployeeId,
        String approverRoleCode,
        String decision,
        String decisionNote,
        LocalDateTime actedAt,
        String status
) {
}