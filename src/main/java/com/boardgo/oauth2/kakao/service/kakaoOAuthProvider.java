package com.boardgo.oauth2.kakao.service;

import static com.boardgo.oauth2.kakao.KakaoOAuthProperties.*;

import com.boardgo.oauth2.kakao.KakaoClient;
import com.boardgo.oauth2.kakao.KakaoOAuthProperties;
import com.boardgo.oauth2.kakao.dto.KakaoAccessTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class kakaoOAuthProvider {

    private final KakaoOAuthProperties properties;
    private final KakaoClient kakaoClient;

    public KakaoAccessTokenResponse getAccessToken(String authorizationCode) {
        // TODO custom Exception 구현
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUri());
        body.add("code", authorizationCode);

        // TODO RestClient로 구현
        kakaoClient.getAccessToken(body);
        return new KakaoAccessTokenResponse(null);
    }
}
