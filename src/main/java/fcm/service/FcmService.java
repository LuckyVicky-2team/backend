package fcm.service;

import static fcm.constant.FcmConstant.WEB_TTL;

import com.boardgo.common.exception.FcmException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushFcmOptions;
import fcm.request.FcmMessageSendRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FcmService {
    private final FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();

    public String sendFcmMessage(FcmMessageSendRequest request) {
        Message message =
                Message.builder()
                        .setToken(request.token())
                        .setNotification(
                                Notification.builder()
                                        .setTitle(request.title())
                                        .setBody(request.content())
                                        .build())
                        .setWebpushConfig(setWebpushConfig(request.pathUrl()))
                        .build();

        try {
            log.info("Send FCM Message :: " + message);
            String response = firebaseMessaging.send(message);
            log.info("Sent FCM response :: " + response);
            return response; // TODO FCM 발송 성공 결과 저장
        } catch (FirebaseMessagingException e) {
            // TODO FCM 발송 실패 예외처리
            throw new FcmException(e.getMessage());
        }
    }

    private WebpushConfig setWebpushConfig(String pathUrl) {
        return WebpushConfig.builder()
                .putHeader("TTL", WEB_TTL)
                .setFcmOptions(WebpushFcmOptions.builder().setLink(pathUrl).build())
                .build();
    }
}
