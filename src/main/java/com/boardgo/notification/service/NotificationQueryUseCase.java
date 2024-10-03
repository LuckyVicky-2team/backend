package com.boardgo.notification.service;

import com.boardgo.notification.service.response.NotificationResponse;
import java.util.List;

public interface NotificationQueryUseCase {
    List<NotificationResponse> getNotificationList(Long userId);
}
