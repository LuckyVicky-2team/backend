package com.boardgo.oauth2.kakao;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

@ConfigurationProperties(prefix = "auth.kakao")
@ConfigurationPropertiesBinding
public record KakaoOAuthProperties(
        String baseUrl,
        String tokenRequestUri,
        String userInfoRequestUri,
        String redirectUri,
        String clientId) {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String GRANT_TYPE = "authorization_code";
    public static final String TOKEN_TYPE = "Bearer ";
}
