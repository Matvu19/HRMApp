package com.hrmapp.api.leave.controller;

import com.hrmapp.api.common.ApiResponse;
import com.hrmapp.api.leave.dto.LeaveRequestCreateRequest;
import com.hrmapp.api.leave.dto.LeaveRequestResponse;
import com.hrmapp.api.leave.service.LeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping("/requests")
    public ApiResponse<LeaveRequestResponse> create(@Valid @RequestBody LeaveRequestCreateRequest request) {
        return new ApiResponse<>(true, "Leave request submitted", leaveService.create(request));
    }

    @GetMapping("/requests/{employeeId}")
    public ApiResponse<List<LeaveRequestResponse>> myRequests(@PathVariable Long employeeId) {
        return new ApiResponse<>(true, "Leave request list", leaveService.myRequests(employeeId));
    }
}