package com.hrmapp.api.notification.dto;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long notificationEventId,
        String eventCode,
        Long recipientUserId,
        String title,
        String messageText,
        String deeplinkUrl,
        String pushStatus,
        LocalDateTime sentAt,
        LocalDateTime readAt,
        String status
) {
}