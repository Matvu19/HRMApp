package com.hrmapp.api.repository;

import com.hrmapp.api.entity.ApprovalStepEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalStepRepository extends JpaRepository<ApprovalStepEntity, Long> {

    List<ApprovalStepEntity> findByApproverEmployeeIdAndStatusOrderByApprovalStepIdDesc(Long approverEmployeeId, String status);

    List<ApprovalStepEntity> findByApprovalFlowIdOrderByStepNoAsc(Long approvalFlowId);
}