package com.hrmapp.api.employee.service;

import com.hrmapp.api.employee.dto.EmployeeProfileResponse;
import com.hrmapp.api.entity.EmployeeProfileEntity;
import com.hrmapp.api.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeProfileService {

    private final EmployeeRepository employeeRepository;

    public List<EmployeeProfileResponse> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    public EmployeeProfileResponse getById(Long employeeId) {
        EmployeeProfileEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        return map(employee);
    }

    public EmployeeProfileResponse getByEmployeeCode(String employeeCode) {
        EmployeeProfileEntity employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        return map(employee);
    }

    private EmployeeProfileResponse map(EmployeeProfileEntity employee) {
        return new EmployeeProfileResponse(
                employee.getEmployeeId(),
                employee.getEmployeeCode(),
                employee.getFullName(),
                employee.getDob(),
                employee.getGender(),
                employee.getWorkEmail(),
                employee.getPersonalPhone(),
                employee.getOrgUnitId(),
                employee.getPositionId(),
                employee.getManagerEmployeeId(),
                employee.getJoinDate(),
                employee.getAvatarUrl(),
                employee.getStatus()
        );
    }
}