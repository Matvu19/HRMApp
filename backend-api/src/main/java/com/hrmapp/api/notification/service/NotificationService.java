package com.hrmapp.api.notification.service;

import com.hrmapp.api.entity.NotificationEventEntity;
import com.hrmapp.api.notification.dto.NotificationResponse;
import com.hrmapp.api.repository.NotificationEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationEventRepository notificationEventRepository;

    public List<NotificationResponse> byUser(Long userId) {
        return notificationEventRepository.findByRecipientUserIdOrderByNotificationEventIdDesc(userId)
                .stream()
                .map(this::map)
                .toList();
    }

    public NotificationResponse markRead(Long notificationId) {
        NotificationEventEntity entity = notificationEventRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        entity.setReadAt(LocalDateTime.now());
        notificationEventRepository.save(entity);
        return map(entity);
    }

    private NotificationResponse map(NotificationEventEntity entity) {
        return new NotificationResponse(
                entity.getNotificationEventId(),
                entity.getEventCode(),
                entity.getRecipientUserId(),
                entity.getTitle(),
                entity.getMessageText(),
                entity.getDeeplinkUrl(),
                entity.getPushStatus(),
                entity.getSentAt(),
                entity.getReadAt(),
                entity.getStatus()
        );
    }
}