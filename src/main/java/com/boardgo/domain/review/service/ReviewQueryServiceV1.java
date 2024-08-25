package com.boardgo.domain.review.service;

import static com.boardgo.domain.meeting.entity.ParticipantType.LEADER;
import static com.boardgo.domain.meeting.entity.ParticipantType.PARTICIPANT;

import com.boardgo.common.exception.CustomNoSuchElementException;
import com.boardgo.domain.mapper.ReviewMapper;
import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.repository.MeetingParticipantRepository;
import com.boardgo.domain.meeting.repository.MeetingRepository;
import com.boardgo.domain.meeting.repository.projection.MeetingReviewProjection;
import com.boardgo.domain.meeting.repository.projection.ParticipationCountProjection;
import com.boardgo.domain.review.entity.enums.ReviewType;
import com.boardgo.domain.review.repository.ReviewRepository;
import com.boardgo.domain.review.repository.projection.ReviewCountProjection;
import com.boardgo.domain.review.service.response.ReviewMeetingResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewQueryServiceV1 implements ReviewUseCase {

    private final ReviewRepository reviewRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingParticipantRepository meetingParticipantRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public List<ReviewMeetingResponse> getReviewMeetings(ReviewType reviewType, Long userId) {
        switch (reviewType) {
            case PRE_PROGRESS -> {
                List<Long> reviewFinishedMeetings = getReviewFinishedList(userId);
                List<MeetingReviewProjection> meetingPreProgressReview =
                        meetingRepository.findMeetingPreProgressReview(
                                userId, reviewFinishedMeetings);
                return reviewMapper.toReviewMeetingReviewMeetingFromProjection(
                        meetingPreProgressReview);
            }
            case FINISH -> {
                List<Long> meetingIds = reviewRepository.findFinishedReview(userId);
                List<MeetingEntity> meetingEntityList = meetingRepository.findAllById(meetingIds);
                return reviewMapper.toReviewMeetingReviewMeetingFromEntity(meetingEntityList);
            }
        }

        throw new CustomNoSuchElementException("리뷰");
    }

    /***
     * 참여한 모임애서 참여자 모두에게 리뷰를 작성한 모임 찾기
     * @param userId
     * @return 리뷰 작성 완료 모임 ID 목록
     */
    private List<Long> getReviewFinishedList(Long userId) {
        List<ReviewCountProjection> reviewCountList =
                reviewRepository.countReviewByReviewerId(userId);
        Map<Long, Long> reviewCountMap =
                reviewCountList.stream()
                        .collect(
                                Collectors.toMap(
                                        ReviewCountProjection::meetingId,
                                        ReviewCountProjection::reviewCount));

        // TODO 현재 종료된 모임만 리뷰를 작성해야할 목록에 있다
        List<ParticipationCountProjection> participationCountList =
                meetingParticipantRepository.countMeetingParticipation(
                        reviewCountMap.keySet(), List.of(LEADER, PARTICIPANT));

        List<Long> reviewFinished = new ArrayList<>();
        for (ParticipationCountProjection participationCount : participationCountList) {
            Long meetingId = participationCount.meetingId();
            Long reviewCount = reviewCountMap.get(meetingId);
            Long participantCount = participationCount.participationCount() - 1; // 본인 제외
            if (reviewCount == participantCount) {
                reviewFinished.add(meetingId);
            }
        }
        return reviewFinished;
    }
}
