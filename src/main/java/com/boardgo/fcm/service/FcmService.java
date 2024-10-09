package com.boardgo.fcm.service;

import static com.boardgo.fcm.constant.FcmConstant.WEB_TTL;

import com.boardgo.common.exception.FcmException;
import com.boardgo.fcm.request.FcmMessageSendRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushFcmOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FcmService implements FcmUseCase {
    private final FirebaseMessaging firebaseMessaging;

    @Override
    public String sendFcmMessage(FcmMessageSendRequest request) {
        Message message =
                Message.builder()
                        .setToken(request.token())
                        .setNotification(setNotification(request.title(), request.content()))
                        .setWebpushConfig(setWebpushConfig(request.pathUrl()))
                        .build();

        try {
            log.info("Send FCM Message :: " + message.toString());
            String response = firebaseMessaging.send(message);
            log.info("Sent FCM response :: " + response);
            return response; // TODO FCM 발송 성공 결과 저장
        } catch (FirebaseMessagingException e) {
            // TODO FCM 발송 실패 예외처리
            throw new FcmException(e.getMessage());
        }
    }

    private Notification setNotification(String title, String body) {
        return Notification.builder().setTitle(title).setBody(body).build();
    }

    private WebpushConfig setWebpushConfig(String pathUrl) {
        return WebpushConfig.builder()
                .putHeader("TTL", WEB_TTL)
                .setFcmOptions(WebpushFcmOptions.builder().setLink(pathUrl).build())
                .build();
    }
}
