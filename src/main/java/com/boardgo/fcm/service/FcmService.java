package com.boardgo.fcm.service;

import static com.boardgo.fcm.constant.FcmConstant.WEB_TTL;

import com.boardgo.fcm.request.FcmMessageSendRequest;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushFcmOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!test")
@RequiredArgsConstructor
public class FcmService {
    private final FirebaseMessagingUseCase firebaseMessagingUseCase;

    public String sendFcmMessage(FcmMessageSendRequest request) {
        Message message =
                Message.builder()
                        .setToken(request.token())
                        .setNotification(setNotification(request.title(), request.content()))
                        .setWebpushConfig(setWebpushConfig(request.pathUrl()))
                        .build();
        return firebaseMessagingUseCase.send(message);
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
