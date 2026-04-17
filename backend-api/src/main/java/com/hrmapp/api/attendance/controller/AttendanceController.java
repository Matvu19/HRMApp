package com.hrmapp.api.attendance.controller;

import com.hrmapp.api.attendance.dto.AttendanceCheckRequest;
import com.hrmapp.api.attendance.dto.AttendanceEventResponse;
import com.hrmapp.api.attendance.service.AttendanceService;
import com.hrmapp.api.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/check")
    public ApiResponse<AttendanceEventResponse> check(@Valid @RequestBody AttendanceCheckRequest request) {
        return new ApiResponse<>(true, "Attendance recorded", attendanceService.check(request));
    }

    @GetMapping("/history/{employeeId}")
    public ApiResponse<List<AttendanceEventResponse>> history(@PathVariable Long employeeId) {
        return new ApiResponse<>(true, "Attendance history", attendanceService.history(employeeId));
    }
}