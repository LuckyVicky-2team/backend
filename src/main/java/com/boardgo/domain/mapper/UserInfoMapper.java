package com.boardgo.domain.mapper;

import com.boardgo.domain.review.service.response.MyEvaluationTagsResponse;
import com.boardgo.domain.user.controller.request.SignupRequest;
import com.boardgo.domain.user.entity.UserInfoEntity;
import com.boardgo.domain.user.repository.projection.PersonalInfoProjection;
import com.boardgo.domain.user.service.response.OtherPersonalInfoResponse;
import com.boardgo.domain.user.service.response.UserInfoResponse;
import com.boardgo.domain.user.service.response.UserPersonalInfoResponse;
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
            defaultExpression = "java(com.boardgo.domain.user.entity.enums.ProviderType.LOCAL)")
    UserInfoEntity toUserInfoEntity(SignupRequest signupRequest);

    UserInfoEntity toUserInfoEntity(OAuth2CreateUserRequest oAuth2CreateUserRequest);

    UserInfoResponse toUserInfoResponse(PersonalInfoProjection personalInfoProjection);

    UserPersonalInfoResponse toUserPersonalInfoResponse(
            UserInfoResponse userInfoResponse, Double averageRating);

    OtherPersonalInfoResponse toUserPersonalInfoResponse(
            UserPersonalInfoResponse userPersonalInfoResponse,
            int meetingCount,
            MyEvaluationTagsResponse myEvaluationTags);
}
