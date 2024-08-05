package com.boardgo.integration.kakao;

import static org.assertj.core.api.Assertions.*;

import com.boardgo.domain.kakao.KakaoOAuthProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KakaoOAuthPropertiesTest {

    @Autowired private KakaoOAuthProperties kakaoOAuthProperties;

    @Test
    @DisplayName("KakaoOAuthProperties uri 조회 테스트")
    void KakaoOAuthProperties_uri_조회_테스트() {
        // given
        String TOKEN_REQUEST_URI = kakaoOAuthProperties.tokenRequestUri();
        String USER_INFO_REQUEST_URI = kakaoOAuthProperties.userInfoRequestUri();

        // when

        // then
        assertThat(TOKEN_REQUEST_URI).isEqualTo("https://kauth.kakao.com/oauth/token");
        assertThat(USER_INFO_REQUEST_URI).isEqualTo("https://kapi.kakao.com/v2/user/me");
    }
}
