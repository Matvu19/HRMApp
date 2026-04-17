package com.hrmapp.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "employee_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    private String employeeCode;

    private String fullName;

    private LocalDate dob;

    private String gender;

    private String workEmail;

    private String personalPhone;

    private Long orgUnitId;

    private Long positionId;

    private Long managerEmployeeId;

    private LocalDate joinDate;

    private String avatarUrl;

    private String status;
}