package com.hrmapp.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "approval_flow")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalFlowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalFlowId;

    private String businessType;

    private Long businessId;

    private Long requesterEmployeeId;

    private Integer currentStepNo;

    private String finalDecision;

    private LocalDateTime submittedAt;

    private LocalDateTime dueAt;

    private String status;
}