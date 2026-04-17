package com.hrmapp.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    private Long employeeId;

    private Long assignmentId;

    private String eventType;

    private LocalDateTime eventTimeDevice;

    private LocalDateTime eventTimeServer;

    private BigDecimal geoLat;

    private BigDecimal geoLng;

    private String deviceId;

    private String idempotencyKey;

    private String sourceType;

    private String status;
}