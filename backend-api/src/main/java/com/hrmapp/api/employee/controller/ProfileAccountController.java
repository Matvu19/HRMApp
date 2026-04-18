package com.hrmapp.api.employee.controller;

import com.hrmapp.api.employee.dto.ChangePasswordRequest;
import com.hrmapp.api.employee.dto.UpdateEmployeeProfileRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProfileAccountController {

    @PutMapping("/employees/{employeeId}")
    public Map<String, Object> updateEmployeeProfile(
            @PathVariable Long employeeId,
            @RequestBody UpdateEmployeeProfileRequest request
    ) {
        Map<String, Object> data = new HashMap<>();
        data.put("employeeId", employeeId);
        data.put("employeeCode", "N/A");
        data.put("fullName", request.getFullName());
        data.put("dob", null);
        data.put("gender", request.getGender());
        data.put("workEmail", null);
        data.put("personalPhone", request.getPersonalPhone());
        data.put("orgUnitId", null);
        data.put("positionId", null);
        data.put("managerEmployeeId", null);
        data.put("joinDate", null);
        data.put("avatarUrl", null);
        data.put("status", "ACTIVE");

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Đã cập nhật thông tin cá nhân");
        result.put("data", data);
        return result;
    }

    @PostMapping("/account/change-password")
    public Map<String, Object> changePassword(
            @RequestBody ChangePasswordRequest request
    ) {
        Map<String, Object> result = new HashMap<>();

        if (request.getCurrentPassword() == null || request.getCurrentPassword().isBlank()) {
            result.put("success", false);
            result.put("message", "Thiếu mật khẩu hiện tại");
            return result;
        }

        if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
            result.put("success", false);
            result.put("message", "Thiếu mật khẩu mới");
            return result;
        }

        result.put("success", true);
        result.put("message", "Đổi mật khẩu thành công");
        return result;
    }
}