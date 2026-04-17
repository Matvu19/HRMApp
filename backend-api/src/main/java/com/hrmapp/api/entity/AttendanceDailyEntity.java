package com.hrmapp.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "attendance_daily")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceDailyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceDailyId;

    private Long employeeId;

    private LocalDate workDate;

    private Integer plannedMinutes;

    private Integer workedMinutes;

    private Integer lateMinutes;

    private Integer earlyLeaveMinutes;

    private String attendanceStatus;

    private String status;
}