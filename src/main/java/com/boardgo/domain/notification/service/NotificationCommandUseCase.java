package com.boardgo.domain.notification.service;

import java.util.List;

public interface NotificationCommandUseCase {
    void readNotification(List<Long> notificationIds);

    void saveNotificationResult(Long notificationId, String result);
}
