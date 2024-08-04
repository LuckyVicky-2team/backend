package com.boardgo.domain.oauth2.service;

import com.boardgo.domain.oauth2.dto.OAuth2Response;
import com.boardgo.domain.oauth2.dto.OAuth2UserResponseFactory;
import com.boardgo.domain.oauth2.entity.CustomOAuth2User;
import com.boardgo.domain.oauth2.entity.ProviderType;
import com.boardgo.domain.user.entity.UserInfoEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        return this.process(userRequest, oAuth2User);
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        ProviderType providerType =
                ProviderType.valueOf(
                        userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2Response oAuth2Response =
                OAuth2UserResponseFactory.getOAuth2Response(
                        providerType, oAuth2User.getAttributes());

        // TODO. User 정보 DB 저장
        UserInfoEntity userInfoEntity = null;

        return CustomOAuth2User.create(userInfoEntity, oAuth2User.getAttributes());
    }
}
