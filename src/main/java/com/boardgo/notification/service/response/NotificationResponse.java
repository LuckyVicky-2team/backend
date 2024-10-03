package com.boardgo.notification.service.response;

public record NotificationResponse(
        Long notificationId, String title, String content, Boolean isRead, String pathUrl) {}
