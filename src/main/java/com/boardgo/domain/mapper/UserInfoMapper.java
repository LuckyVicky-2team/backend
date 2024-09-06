package com.boardgo.domain.mapper;

import com.boardgo.domain.meeting.service.response.UserParticipantResponse;
import com.boardgo.domain.review.service.response.MyEvaluationTagsResponse;
import com.boardgo.domain.user.controller.request.SignupRequest;
import com.boardgo.domain.user.entity.UserInfoEntity;
import com.boardgo.domain.user.repository.projection.PersonalInfoProjection;
import com.boardgo.domain.user.repository.projection.UserParticipantProjection;
import com.boardgo.domain.user.service.response.OtherPersonalInfoResponse;
import com.boardgo.domain.user.service.response.UserPersonalInfoResponse;
import com.boardgo.oauth2.dto.OAuth2CreateUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
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

    UserPersonalInfoResponse toUserPersonalInfoResponse(
            PersonalInfoProjection personalInfoProjection);

    @Mapping(target = "averageRating", source = "avgRating", qualifiedByName = "setAvgRating")
    UserPersonalInfoResponse toUserPersonalInfoResponse(
            UserPersonalInfoResponse userPersonalInfoResponse, Double avgRating);

    @Named("setAvgRating")
    static Double setAvgRating(Double avgRating) {
        return avgRating;
    }

    UserParticipantResponse toUserParticipantResponse(
            UserParticipantProjection userParticipantProjection);

    OtherPersonalInfoResponse toUserPersonalInfoResponse(
            UserPersonalInfoResponse userPersonalInfoResponse,
            int meetingCount,
            MyEvaluationTagsResponse myEvaluationTags);
}
