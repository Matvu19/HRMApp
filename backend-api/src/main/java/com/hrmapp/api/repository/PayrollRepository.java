package com.hrmapp.api.repository;

import com.hrmapp.api.entity.PayrollRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayrollRepository extends JpaRepository<PayrollRecord, Long> {
    List<PayrollRecord> findByEmployeeIdOrderByPayrollMonthDesc(Long employeeId);
}