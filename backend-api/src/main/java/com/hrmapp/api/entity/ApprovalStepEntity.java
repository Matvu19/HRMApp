package com.hrmapp.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "approval_step")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalStepEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalStepId;

    private Long approvalFlowId;

    private Integer stepNo;

    private Long approverEmployeeId;

    private String approverRoleCode;

    private String decision;

    @Column(columnDefinition = "TEXT")
    private String decisionNote;

    private LocalDateTime actedAt;

    private String status;
}