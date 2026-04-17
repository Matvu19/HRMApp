package com.hrmapp.api.employee.controller;

import com.hrmapp.api.common.ApiResponse;
import com.hrmapp.api.employee.dto.EmployeeProfileResponse;
import com.hrmapp.api.employee.service.EmployeeProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeProfileController {

    private final EmployeeProfileService employeeProfileService;

    @GetMapping
    public ApiResponse<List<EmployeeProfileResponse>> getAllEmployees() {
        return new ApiResponse<>(
                true,
                "Employees fetched successfully",
                employeeProfileService.getAllEmployees()
        );
    }

    @GetMapping("/{employeeId}")
    public ApiResponse<EmployeeProfileResponse> getEmployeeById(@PathVariable Long employeeId) {
        return new ApiResponse<>(
                true,
                "Employee fetched successfully",
                employeeProfileService.getById(employeeId)
        );
    }

    @GetMapping("/code/{employeeCode}")
    public ApiResponse<EmployeeProfileResponse> getEmployeeByCode(@PathVariable String employeeCode) {
        return new ApiResponse<>(
                true,
                "Employee fetched successfully",
                    employeeProfileService.getByEmployeeCode(employeeCode)
        );
    }
}