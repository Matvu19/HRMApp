package com.hrmapp.api.approval.service;

import com.hrmapp.api.approval.dto.ApprovalActionRequest;
import com.hrmapp.api.approval.dto.ApprovalStepResponse;
import com.hrmapp.api.entity.ApprovalFlowEntity;
import com.hrmapp.api.entity.ApprovalStepEntity;
import com.hrmapp.api.entity.LeaveRequestEntity;
import com.hrmapp.api.repository.ApprovalFlowRepository;
import com.hrmapp.api.repository.ApprovalStepRepository;
import com.hrmapp.api.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalService {

    private final ApprovalStepRepository approvalStepRepository;
    private final ApprovalFlowRepository approvalFlowRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    public List<ApprovalStepResponse> pendingByApprover(Long approverEmployeeId) {
        return approvalStepRepository.findByApproverEmployeeIdAndStatusOrderByApprovalStepIdDesc(approverEmployeeId, "PENDING")
                .stream()
                .map(this::map)
                .toList();
    }

    @Transactional
    public ApprovalStepResponse action(ApprovalActionRequest request) {
        ApprovalStepEntity step = approvalStepRepository.findById(request.approvalStepId())
                .orElseThrow(() -> new RuntimeException("Approval step not found"));

        step.setDecision(request.decision());
        step.setDecisionNote(request.decisionNote());
        step.setActedAt(LocalDateTime.now());
        step.setStatus("DONE");
        approvalStepRepository.save(step);

        ApprovalFlowEntity flow = approvalFlowRepository.findById(step.getApprovalFlowId())
                .orElseThrow(() -> new RuntimeException("Approval flow not found"));

        String finalDecision = "APPROVED";
        if ("REJECTED".equalsIgnoreCase(request.decision())) {
            finalDecision = "REJECTED";
        }

        flow.setFinalDecision(finalDecision);
        flow.setStatus(finalDecision);
        approvalFlowRepository.save(flow);

        if ("LEAVE_REQUEST".equalsIgnoreCase(flow.getBusinessType())) {
            LeaveRequestEntity leave = leaveRequestRepository.findById(flow.getBusinessId())
                    .orElseThrow(() -> new RuntimeException("Leave request not found"));
            leave.setStatus(finalDecision);
            leaveRequestRepository.save(leave);
        }

        return map(step);
    }

    private ApprovalStepResponse map(ApprovalStepEntity step) {
        return new ApprovalStepResponse(
                step.getApprovalStepId(),
                step.getApprovalFlowId(),
                step.getStepNo(),
                step.getApproverEmployeeId(),
                step.getApproverRoleCode(),
                step.getDecision(),
                step.getDecisionNote(),
                step.getActedAt(),
                step.getStatus()
        );
    }
}