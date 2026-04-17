package com.hrmapp.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "leave_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveRequestId;

    private Long employeeId;

    private Long leaveTypeId;

    private Long approvalFlowId;

    private LocalDate dateFrom;

    private LocalDate dateTo;

    private BigDecimal durationDays;

    @Column(columnDefinition = "TEXT")
    private String reasonText;

    private Long attachmentFileId;

    private String status;
}