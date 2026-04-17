package com.hrmapp.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationEventId;

    private String eventCode;

    private Long recipientUserId;

    private String title;

    private String messageText;

    private String deeplinkUrl;

    private String pushStatus;

    private LocalDateTime sentAt;

    private LocalDateTime readAt;

    private String status;
}