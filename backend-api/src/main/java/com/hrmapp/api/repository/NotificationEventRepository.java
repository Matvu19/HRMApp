package com.hrmapp.api.repository;

import com.hrmapp.api.entity.NotificationEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationEventRepository extends JpaRepository<NotificationEventEntity, Long> {

    List<NotificationEventEntity> findByRecipientUserIdOrderByNotificationEventIdDesc(Long recipientUserId);
}