package com.hrmapp.api.repository;

import com.hrmapp.api.entity.AttendanceEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceEventRepository extends JpaRepository<AttendanceEventEntity, Long> {

    List<AttendanceEventEntity> findByEmployeeIdOrderByEventTimeServerDesc(Long employeeId);

    Optional<AttendanceEventEntity> findByIdempotencyKey(String idempotencyKey);
}