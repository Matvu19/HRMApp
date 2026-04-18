package com.hrmapp.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payroll_record")
@Getter
@Setter
public class PayrollRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payrollId;

    private Long employeeId;

    private String payrollMonth;

    private Double grossSalary;

    private Double allowanceAmount;

    private Double deductionAmount;

    private Double netSalary;

    private String status;
}