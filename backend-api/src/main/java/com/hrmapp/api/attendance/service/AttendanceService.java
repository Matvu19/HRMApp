package com.hrmapp.api.attendance.service;

import com.hrmapp.api.attendance.dto.AttendanceCheckRequest;
import com.hrmapp.api.attendance.dto.AttendanceEventResponse;
import com.hrmapp.api.entity.AttendanceEventEntity;
import com.hrmapp.api.repository.AttendanceEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceEventRepository attendanceEventRepository;

    public AttendanceEventResponse check(AttendanceCheckRequest request) {
        AttendanceEventEntity existed = attendanceEventRepository.findByIdempotencyKey(request.idempotencyKey())
                .orElse(null);

        if (existed != null) {
            return map(existed);
        }

        AttendanceEventEntity saved = attendanceEventRepository.save(
                AttendanceEventEntity.builder()
                        .employeeId(request.employeeId())
                        .assignmentId(request.assignmentId())
                        .eventType(request.eventType())
                        .eventTimeDevice(request.eventTimeDevice())
                        .eventTimeServer(LocalDateTime.now())
                        .geoLat(request.geoLat())
                        .geoLng(request.geoLng())
                        .deviceId(request.deviceId())
                        .idempotencyKey(request.idempotencyKey())
                        .sourceType("MOBILE")
                        .status("RECORDED")
                        .build()
        );

        return map(saved);
    }

    public List<AttendanceEventResponse> history(Long employeeId) {
        return attendanceEventRepository.findByEmployeeIdOrderByEventTimeServerDesc(employeeId)
                .stream()
                .map(this::map)
                .toList();
    }

    private AttendanceEventResponse map(AttendanceEventEntity event) {
        return new AttendanceEventResponse(
                event.getEventId(),
                event.getEmployeeId(),
                event.getAssignmentId(),
                event.getEventType(),
                event.getEventTimeDevice(),
                event.getEventTimeServer(),
                event.getGeoLat(),
                event.getGeoLng(),
                event.getStatus()
        );
    }
}