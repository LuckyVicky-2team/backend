package com.boardgo.domain.notification.service;

import com.boardgo.common.exception.CustomNoSuchElementException;
import com.boardgo.domain.notification.entity.NotificationEntity;
import com.boardgo.domain.notification.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationCommandServiceV1 implements NotificationCommandUseCase {
    private final NotificationRepository notificationRepository;

    @Override
    public void readNotification(List<Long> notificationIds) {
        List<NotificationEntity> notificationEntity =
                notificationRepository.findAllByIdInAndIsReadAndIsSent(
                        notificationIds, false, true);
        if (notificationEntity.isEmpty()) {
            throw new CustomNoSuchElementException("알림");
        }
        List<Long> ids = notificationEntity.stream().map(NotificationEntity::getId).toList();
        notificationRepository.readNotifications(ids);
    }

    @Override
    public void saveNotificationResult(Long notificationId, String result) {
        Optional<NotificationEntity> notificationOptional =
                notificationRepository.findById(notificationId);
        if (notificationOptional.isPresent()) {
            NotificationEntity notification = notificationOptional.get();
            notification.sent();
            notification.saveRawResult(result);
        }
    }
}
