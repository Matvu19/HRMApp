package com.hrmapp.api.controller;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    @GetMapping("/employee/{employeeId}")
    public Map<String, Object> getPayrollByEmployee(@PathVariable Long employeeId) {

        List<Map<String, Object>> items = new ArrayList<>();

        Map<String, Object> p1 = new HashMap<>();
        p1.put("payrollId", 1);
        p1.put("employeeId", employeeId);
        p1.put("payrollMonth", "2026-03");
        p1.put("grossSalary", 15000000);
        p1.put("allowanceAmount", 2000000);
        p1.put("deductionAmount", 1000000);
        p1.put("netSalary", 16000000);
        p1.put("status", "PAID");

        Map<String, Object> p2 = new HashMap<>();
        p2.put("payrollId", 2);
        p2.put("employeeId", employeeId);
        p2.put("payrollMonth", "2026-04");
        p2.put("grossSalary", 15000000);
        p2.put("allowanceAmount", 2500000);
        p2.put("deductionAmount", 1200000);
        p2.put("netSalary", 16300000);
        p2.put("status", "PAID");

        items.add(p1);
        items.add(p2);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Mock payroll");
        result.put("data", items);

        return result;
    }
}