package com.hrmapp.api.leave.service;

import com.hrmapp.api.entity.ApprovalFlowEntity;
import com.hrmapp.api.entity.ApprovalStepEntity;
import com.hrmapp.api.entity.EmployeeProfileEntity;
import com.hrmapp.api.entity.LeaveRequestEntity;
import com.hrmapp.api.leave.dto.LeaveRequestCreateRequest;
import com.hrmapp.api.leave.dto.LeaveRequestResponse;
import com.hrmapp.api.repository.ApprovalFlowRepository;
import com.hrmapp.api.repository.ApprovalStepRepository;
import com.hrmapp.api.repository.EmployeeRepository;
import com.hrmapp.api.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final ApprovalFlowRepository approvalFlowRepository;
    private final ApprovalStepRepository approvalStepRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public LeaveRequestResponse create(LeaveRequestCreateRequest request) {
        EmployeeProfileEntity employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LeaveRequestEntity savedLeave = leaveRequestRepository.save(
                LeaveRequestEntity.builder()
                        .employeeId(request.employeeId())
                        .leaveTypeId(request.leaveTypeId())
                        .dateFrom(request.dateFrom())
                        .dateTo(request.dateTo())
                        .durationDays(request.durationDays())
                        .reasonText(request.reasonText())
                        .status("SUBMITTED")
                        .build()
        );

        ApprovalFlowEntity flow = approvalFlowRepository.save(
                ApprovalFlowEntity.builder()
                        .businessType("LEAVE_REQUEST")
                        .businessId(savedLeave.getLeaveRequestId())
                        .requesterEmployeeId(request.employeeId())
                        .currentStepNo(1)
                        .finalDecision("PENDING")
                        .submittedAt(LocalDateTime.now())
                        .dueAt(LocalDateTime.now().plusDays(2))
                        .status("PENDING")
                        .build()
        );

        savedLeave.setApprovalFlowId(flow.getApprovalFlowId());
        leaveRequestRepository.save(savedLeave);

        approvalStepRepository.save(
                ApprovalStepEntity.builder()
                        .approvalFlowId(flow.getApprovalFlowId())
                        .stepNo(1)
                        .approverEmployeeId(employee.getManagerEmployeeId())
                        .approverRoleCode("MANAGER")
                        .status("PENDING")
                        .build()
        );

        return map(savedLeave);
    }

    public List<LeaveRequestResponse> myRequests(Long employeeId) {
        return leaveRequestRepository.findByEmployeeIdOrderByLeaveRequestIdDesc(employeeId)
                .stream()
                .map(this::map)
                .toList();
    }

    private LeaveRequestResponse map(LeaveRequestEntity leave) {
        return new LeaveRequestResponse(
                leave.getLeaveRequestId(),
                leave.getEmployeeId(),
                leave.getLeaveTypeId(),
                leave.getApprovalFlowId(),
                leave.getDateFrom(),
                leave.getDateTo(),
                leave.getDurationDays(),
                leave.getReasonText(),
                leave.getStatus()
        );
    }
}