package com.hrmapp.api.approval.controller;

import com.hrmapp.api.approval.dto.ApprovalActionRequest;
import com.hrmapp.api.approval.dto.ApprovalStepResponse;
import com.hrmapp.api.approval.service.ApprovalService;
import com.hrmapp.api.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/approvals")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    @GetMapping("/pending/{approverEmployeeId}")
    public ApiResponse<List<ApprovalStepResponse>> pending(@PathVariable Long approverEmployeeId) {
        return new ApiResponse<>(true, "Pending approvals", approvalService.pendingByApprover(approverEmployeeId));
    }

    @PostMapping("/action")
    public ApiResponse<ApprovalStepResponse> action(@Valid @RequestBody ApprovalActionRequest request) {
        return new ApiResponse<>(true, "Approval action success", approvalService.action(request));
    }
}