package com.hrmapp.api.approval.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ApprovalActionRequest(
        @NotNull Long approvalStepId,
        @NotBlank String decision,
        String decisionNote
) {
}