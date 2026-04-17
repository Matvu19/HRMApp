package com.hrmapp.api.notification.controller;

import com.hrmapp.api.common.ApiResponse;
import com.hrmapp.api.notification.dto.NotificationResponse;
import com.hrmapp.api.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public ApiResponse<List<NotificationResponse>> byUser(@PathVariable Long userId) {
        return new ApiResponse<>(true, "Notification list", notificationService.byUser(userId));
    }

    @PostMapping("/{notificationId}/read")
    public ApiResponse<NotificationResponse> markRead(@PathVariable Long notificationId) {
        return new ApiResponse<>(true, "Notification marked as read", notificationService.markRead(notificationId));
    }
}