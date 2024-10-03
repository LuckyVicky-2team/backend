package com.boardgo.notification.service;

import com.boardgo.domain.mapper.NotificationMapper;
import com.boardgo.notification.repository.NotificationRepository;
import com.boardgo.notification.service.response.NotificationResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationQueryServiceV1 implements NotificationQueryUseCase {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public List<NotificationResponse> getNotificationList(Long userId) {
        return notificationMapper.toResponseList(notificationRepository.findByUserInfoId(userId));
    }
}
