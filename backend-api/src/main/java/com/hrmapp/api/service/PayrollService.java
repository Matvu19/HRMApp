package com.hrmapp.api.service;

import com.hrmapp.api.entity.PayrollRecord;
import com.hrmapp.api.repository.PayrollRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayrollService {

    private final PayrollRepository payrollRepository;

    public PayrollService(PayrollRepository payrollRepository) {
        this.payrollRepository = payrollRepository;
    }

    public List<PayrollRecord> getPayrollByEmployeeId(Long employeeId) {
        return payrollRepository.findByEmployeeIdOrderByPayrollMonthDesc(employeeId);
    }
}