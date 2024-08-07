package com.boardgo.domain.mapper;

import com.boardgo.domain.user.controller.dto.SignupRequest;
import com.boardgo.domain.user.entity.UserInfoEntity;
import com.boardgo.oauth2.dto.OAuth2CreateUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserInfoMapper {
    UserInfoMapper INSTANCE = Mappers.getMapper(UserInfoMapper.class);

    @Mapping(
            source = "providerType",
            target = "providerType",
            defaultExpression = "java(com.boardgo.domain.user.entity.ProviderType.LOCAL)")
    UserInfoEntity toUserInfoEntity(SignupRequest signupRequest);

    UserInfoEntity toUserInfoEntity(OAuth2CreateUserRequest oAuth2CreateUserRequest);
}
