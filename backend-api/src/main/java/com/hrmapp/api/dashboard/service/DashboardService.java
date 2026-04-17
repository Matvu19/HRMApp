package com.hrmapp.api.dashboard.service;

import com.hrmapp.api.dashboard.dto.ManagerDashboardResponse;
import com.hrmapp.api.entity.EmployeeProfileEntity;
import com.hrmapp.api.entity.NotificationEventEntity;
import com.hrmapp.api.entity.UserAccountEntity;
import com.hrmapp.api.repository.ApprovalStepRepository;
import com.hrmapp.api.repository.EmployeeRepository;
import com.hrmapp.api.repository.NotificationEventRepository;
import com.hrmapp.api.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ApprovalStepRepository approvalStepRepository;
    private final EmployeeRepository employeeRepository;
    private final UserAccountRepository userAccountRepository;
    private final NotificationEventRepository notificationEventRepository;

    public ManagerDashboardResponse managerDashboard(Long managerEmployeeId) {
        int pendingApprovalCount = approvalStepRepository
                .findByApproverEmployeeIdAndStatusOrderByApprovalStepIdDesc(managerEmployeeId, "PENDING")
                .size();

        List<EmployeeProfileEntity> employees = employeeRepository.findAll();
        int teamSize = (int) employees.stream()
                .filter(e -> managerEmployeeId.equals(e.getManagerEmployeeId()))
                .count();

        int todayCheckinCount = teamSize;

        UserAccountEntity managerUser = userAccountRepository.findAll().stream()
                .filter(u -> managerEmployeeId.equals(u.getEmployeeId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Manager user not found"));

        List<NotificationEventEntity> notifications = notificationEventRepository
                .findByRecipientUserIdOrderByNotificationEventIdDesc(managerUser.getUserId());

        int unreadNotificationCount = (int) notifications.stream()
                .filter(n -> n.getReadAt() == null)
                .count();

        return new ManagerDashboardResponse(
                managerEmployeeId,
                pendingApprovalCount,
                todayCheckinCount,
                unreadNotificationCount,
                teamSize
        );
    }
}