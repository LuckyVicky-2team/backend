package com.boardgo.domain.user.service.facade;

import com.boardgo.domain.mapper.UserInfoMapper;
import com.boardgo.domain.meeting.service.MeetingParticipantQueryUseCase;
import com.boardgo.domain.review.service.ReviewUseCase;
import com.boardgo.domain.review.service.response.MyEvaluationTagsResponse;
import com.boardgo.domain.user.service.UserQueryUseCase;
import com.boardgo.domain.user.service.response.OtherPersonalInfoResponse;
import com.boardgo.domain.user.service.response.UserPersonalInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserQueryServiceFacadeImpl implements UserQueryServiceFacade {

    private final UserQueryUseCase userQueryUseCase;
    private final ReviewUseCase reviewUseCase;
    private final MeetingParticipantQueryUseCase meetingParticipantQueryUseCase;
    private final UserInfoMapper userInfoMapper;

    public OtherPersonalInfoResponse getOtherPersonalInfo(Long userId) {
        int meetingCount = meetingParticipantQueryUseCase.getMeetingCount(userId);
        MyEvaluationTagsResponse myEvaluationTags = reviewUseCase.getMyEvaluationTags(userId);
        return userInfoMapper.toUserPersonalInfoResponse(
                getPersonalInfo(userId), meetingCount, myEvaluationTags);
    }

    public UserPersonalInfoResponse getPersonalInfo(Long userId) {
        UserPersonalInfoResponse userPersonalInfo = userQueryUseCase.getPersonalInfo(userId);
        Double averageRating = reviewUseCase.getAverageRating(userId);
        return userInfoMapper.toUserPersonalInfoResponse(userPersonalInfo, averageRating);
    }
}
