package com.boardgo.domain.review.service.facade;

import com.boardgo.domain.mapper.ReviewMapper;
import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.service.MeetingParticipantQueryUseCase;
import com.boardgo.domain.meeting.service.MeetingQueryUseCase;
import com.boardgo.domain.review.entity.enums.ReviewType;
import com.boardgo.domain.review.service.ReviewQueryUseCase;
import com.boardgo.domain.review.service.response.ReviewMeetingParticipantsResponse;
import com.boardgo.domain.review.service.response.ReviewMeetingResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewQueryFacadeImpl implements ReviewQueryFacade {
    private final ReviewQueryUseCase reviewQueryUseCase;
    private final MeetingParticipantQueryUseCase meetingParticipantQueryUseCase;
    private final MeetingQueryUseCase meetingQueryUseCase;
    private final ReviewMapper reviewMapper;

    @Override
    public List<ReviewMeetingParticipantsResponse> getMeetingParticipantsToReview(
            Long meetingId, Long reviewerId) {
        List<Long> revieweeIds =
                reviewQueryUseCase.getReviewMeetingParticipants(meetingId, reviewerId);
        revieweeIds.add(reviewerId); // 본인 리뷰 작성자 목록 표출 제외
        return meetingParticipantQueryUseCase.findMeetingParticipantsToReview(
                revieweeIds, meetingId);
    }

    @Override
    public List<ReviewMeetingResponse> getMeetingsToReview(ReviewType reviewType, Long userId) {
        switch (reviewType) {
            case PRE_PROGRESS -> {
                List<Long> finishedReviewMeetingIds =
                        reviewQueryUseCase.findFinishedReviewMeetingIds(userId);
                return meetingQueryUseCase.findReviewableMeeting(userId, finishedReviewMeetingIds);
            }
            case FINISH -> {
                List<Long> meetingIds = reviewQueryUseCase.findMeetingIdsOfWrittenReview(userId);
                List<MeetingEntity> meetingEntityList = meetingQueryUseCase.findAllById(meetingIds);
                return reviewMapper.toReviewMeetingResponses(meetingEntityList);
            }
        }
        return null;
    }
}
