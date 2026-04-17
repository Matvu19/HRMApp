package com.hrmapp.api.dashboard.dto;

public record ManagerDashboardResponse(
        Long managerEmployeeId,
        int pendingApprovalCount,
        int todayCheckinCount,
        int unreadNotificationCount,
        int teamSize
) {
}