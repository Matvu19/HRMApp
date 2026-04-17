package com.hrmapp.api.repository;

import com.hrmapp.api.entity.LeaveRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequestEntity, Long> {

    List<LeaveRequestEntity> findByEmployeeIdOrderByLeaveRequestIdDesc(Long employeeId);
}