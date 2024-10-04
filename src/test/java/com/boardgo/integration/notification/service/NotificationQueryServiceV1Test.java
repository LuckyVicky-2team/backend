package com.boardgo.integration.notification.service;

import static com.boardgo.integration.data.NotificationData.getNotificationMessage;
import static org.assertj.core.api.Assertions.assertThat;

import com.boardgo.domain.notification.entity.MessageType;
import com.boardgo.domain.notification.entity.NotificationEntity;
import com.boardgo.domain.notification.entity.NotificationMessage;
import com.boardgo.domain.notification.repository.NotificationRepository;
import com.boardgo.domain.notification.service.NotificationQueryUseCase;
import com.boardgo.domain.notification.service.response.NotificationResponse;
import com.boardgo.integration.data.NotificationData;
import com.boardgo.integration.support.IntegrationTestSupport;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class NotificationQueryServiceV1Test extends IntegrationTestSupport {
    @Autowired private NotificationRepository notificationRepository;
    @Autowired private NotificationQueryUseCase notificationQueryUseCase;

    @Test
    @DisplayName("전송된 알림 메세지만 알림 목록에서 조회할 수 있다")
    void 전송된_알림_메세지만_알림_목록에서_조회할_수_있다() {
        // given
        Long userId = 1L;
        NotificationEntity failNotification1 =
                NotificationData.getNotification(
                                2L, getNotificationMessage(MessageType.MEETING_MODIFY).build())
                        .isSent(false)
                        .build();
        NotificationEntity failNotification2 =
                NotificationData.getNotification(
                                userId, getNotificationMessage(MessageType.REQUEST_REVIEW).build())
                        .isSent(false)
                        .pathUrl("/mypage/review")
                        .build();
        NotificationEntity notification3 =
                NotificationData.getNotification(
                                userId, getNotificationMessage(MessageType.REVIEW_RECEIVED).build())
                        .pathUrl("/mypage/review/receivedReviews")
                        .build();

        notificationRepository.save(failNotification1);
        notificationRepository.save(failNotification2);
        notificationRepository.save(notification3);

        // when
        List<NotificationResponse> notificationList =
                notificationQueryUseCase.getNotificationList(1L);

        // then
        assertThat(notificationList).isNotEmpty();
        notificationList.forEach(
                response -> {
                    NotificationEntity notificationEntity =
                            notificationRepository.findById(response.notificationId()).get();
                    assertThat(notificationEntity.getIsSent()).isTrue();
                    assertThat(notificationEntity.getUserInfoId()).isEqualTo(userId);

                    NotificationMessage message = notificationEntity.getMessage();
                    assertThat(response.title()).isEqualTo(message.getTitle());
                    assertThat(response.content()).isEqualTo(message.getContent());
                });
    }

    @Test
    @DisplayName("목록에서 조회 가능한 알림 메세지의 발송시간은 현재보다 과거이다")
    void 목록에서_조회_가능한_알림_메세지의_발송시간은_현재보다_과거이다() {
        // given
        NotificationEntity notification1 =
                NotificationData.getNotification(
                                1L, getNotificationMessage(MessageType.MEETING_MODIFY).build())
                        .build();
        NotificationEntity notification2 =
                NotificationData.getNotification(
                                1L, getNotificationMessage(MessageType.MEETING_REMINDER).build())
                        .build();
        NotificationEntity reservedNotification1 =
                NotificationData.getNotification(
                                1L, getNotificationMessage(MessageType.REQUEST_REVIEW).build())
                        .isSent(false)
                        .pathUrl("/mypage/review")
                        .sendDateTime(LocalDateTime.now().plusHours(3))
                        .build();
        NotificationEntity reservedNotification2 =
                NotificationData.getNotification(
                                1L, getNotificationMessage(MessageType.REQUEST_REVIEW).build())
                        .isSent(false)
                        .pathUrl("/mypage/review")
                        .sendDateTime(LocalDateTime.now().plusHours(3))
                        .build();

        notificationRepository.save(notification1);
        notificationRepository.save(notification2);
        notificationRepository.save(reservedNotification1);
        notificationRepository.save(reservedNotification2);

        // when
        List<NotificationResponse> notificationList =
                notificationQueryUseCase.getNotificationList(1L);

        // then
        assertThat(notificationList).isNotEmpty();
        notificationList.forEach(
                response -> {
                    NotificationEntity notificationEntity =
                            notificationRepository.findById(response.notificationId()).get();
                    assertThat(notificationEntity.getSendDateTime()).isBefore(LocalDateTime.now());
                });
    }
}
