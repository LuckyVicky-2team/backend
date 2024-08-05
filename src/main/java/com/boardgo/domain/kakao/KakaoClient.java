package com.boardgo.domain.kakao;

import com.boardgo.domain.kakao.dto.KakaoAccessTokenResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PostExchange;

@Component
public interface KakaoClient {

    @PostExchange(value = "${auth.kakao.token-request-uri}")
    KakaoAccessTokenResponse getAccessToken(@RequestParam MultiValueMap<String, String> body);

    // @PostExchange(value = "${auth.kakao.user-info-request-uri}")
    // KakaoUserInfoResponse getUserInfo(
    // 	@RequestHeader(value = KakaoOAuthProperties.AUTHORIZATION_HEADER) String token,
    // 	@RequestParam MultiValueMap<String, String> body);
}
