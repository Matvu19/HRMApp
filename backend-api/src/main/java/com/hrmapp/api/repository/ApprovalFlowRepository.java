package com.hrmapp.api.repository;

import com.hrmapp.api.entity.ApprovalFlowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalFlowRepository extends JpaRepository<ApprovalFlowEntity, Long> {

    List<ApprovalFlowEntity> findByRequesterEmployeeIdOrderByApprovalFlowIdDesc(Long requesterEmployeeId);
}