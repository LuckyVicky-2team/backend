package com.boardgo.integration.fcm;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.boardgo.common.exception.FcmException;
import com.boardgo.fcm.request.FcmMessageSendRequest;
import com.boardgo.fcm.service.FcmService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("local")
public class FcmServiceTest {
    @Autowired private FcmService fcmService;

    @Test
    @DisplayName("토큰이 유효하지 않을 경우 fcm 메세지 전송에 실패한다")
    void 토큰이_유효하지_않을_경우_fcm_메세지_전송에_실패한다() {
        // given
        String failToken = "fksdfhsadkjh4gjlfjlsaag";
        FcmMessageSendRequest request =
                new FcmMessageSendRequest(failToken, "푸시 메세지 제목", "푸시 메세지 내용", null);

        // when
        // then
        assertThatThrownBy(() -> fcmService.sendFcmMessage(request))
                .isInstanceOf(FcmException.class);
    }
}
