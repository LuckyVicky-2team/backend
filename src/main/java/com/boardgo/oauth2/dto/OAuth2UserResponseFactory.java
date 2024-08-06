package com.boardgo.oauth2.dto;

import com.boardgo.oauth2.entity.ProviderType;
import java.util.Map;

public class OAuth2UserResponseFactory {
    public static OAuth2Response getOAuth2Response(
            ProviderType providerType, Map<String, Object> attribute) {
        switch (providerType) {
            case GOOGLE:
                return new GoogleResponse(attribute);
            case KAKAO:
                return new KakaoResponse(attribute);
            default:
                // TODO 예외처리 필요
                throw new IllegalArgumentException("Invalid Provider Type");
        }
    }
}
