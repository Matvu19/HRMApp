package com.hrmapp.api.repository;

import com.hrmapp.api.entity.AttendanceDailyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceDailyRepository extends JpaRepository<AttendanceDailyEntity, Long> {

    Optional<AttendanceDailyEntity> findByEmployeeIdAndWorkDate(Long employeeId, LocalDate workDate);
}