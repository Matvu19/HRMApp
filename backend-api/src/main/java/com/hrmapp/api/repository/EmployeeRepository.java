package com.hrmapp.api.repository;

import com.hrmapp.api.entity.EmployeeProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<EmployeeProfileEntity, Long> {

    Optional<EmployeeProfileEntity> findByEmployeeCode(String employeeCode);
}