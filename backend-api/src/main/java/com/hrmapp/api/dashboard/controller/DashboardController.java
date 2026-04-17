package com.hrmapp.api.dashboard.controller;

import com.hrmapp.api.common.ApiResponse;
import com.hrmapp.api.dashboard.dto.ManagerDashboardResponse;
import com.hrmapp.api.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/manager/{managerEmployeeId}")
    public ApiResponse<ManagerDashboardResponse> managerDashboard(@PathVariable Long managerEmployeeId) {
        return new ApiResponse<>(
                true,
                "Manager dashboard",
                dashboardService.managerDashboard(managerEmployeeId)
        );
    }
}