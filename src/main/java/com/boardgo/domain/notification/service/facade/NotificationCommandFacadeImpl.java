package com.boardgo.domain.notification.service.facade;

import com.boardgo.common.exception.FcmException;
import com.boardgo.domain.notification.entity.NotificationEntity;
import com.boardgo.domain.notification.service.NotificationQueryUseCase;
import com.boardgo.domain.notification.service.response.NotificationPushResponse;
import com.boardgo.fcm.request.FcmMessageSendRequest;
import com.boardgo.fcm.service.FcmService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationCommandFacadeImpl implements NotificationCommandFacade {
    private final NotificationQueryUseCase notificationQueryUseCase;
    private final FcmService fcmService;

    @Async
    @Override
    public void sendNotificationPush() {
        List<NotificationPushResponse> notificationPushList =
                notificationQueryUseCase.getNotificationPushList();
        notificationPushList.forEach(
                push -> {
                    String fcmResult = "";
                    try {
                        fcmResult =
                                fcmService.sendFcmMessage(
                                        new FcmMessageSendRequest(
                                                push.token(),
                                                push.title(),
                                                push.content(),
                                                push.pathUrl()));
                        // TODO 하나의 트랜잭션으로 처리: 알림 데이터 is_sent 상태 변경 + fcm 푸시 데이터 저장
                        NotificationEntity notification =
                                notificationQueryUseCase.getNotification(push.notificationId());
                        notification.send();

                    } catch (FcmException fe) {
                        fcmResult = fe.getMessage();
                        // TODO fcm 푸시 실패 시 저장
                    }
                });
    }
}
