package com.boardgo.domain.review.service;

import static com.boardgo.common.utils.CustomStringUtils.stringToLongList;
import static com.boardgo.domain.meeting.entity.enums.ParticipantType.LEADER;
import static com.boardgo.domain.meeting.entity.enums.ParticipantType.PARTICIPANT;

import com.boardgo.common.exception.CustomIllegalArgumentException;
import com.boardgo.common.exception.CustomNoSuchElementException;
import com.boardgo.common.exception.CustomNullPointException;
import com.boardgo.common.exception.DuplicateException;
import com.boardgo.domain.mapper.ReviewMapper;
import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.repository.MeetingParticipantRepository;
import com.boardgo.domain.meeting.repository.MeetingRepository;
import com.boardgo.domain.meeting.repository.projection.MeetingReviewProjection;
import com.boardgo.domain.meeting.repository.projection.ParticipationCountProjection;
import com.boardgo.domain.meeting.repository.projection.ReviewMeetingParticipantsProjection;
import com.boardgo.domain.review.controller.request.ReviewCreateRequest;
import com.boardgo.domain.review.entity.EvaluationTagEntity;
import com.boardgo.domain.review.entity.ReviewEntity;
import com.boardgo.domain.review.entity.enums.ReviewType;
import com.boardgo.domain.review.repository.EvaluationTagRepository;
import com.boardgo.domain.review.repository.ReviewRepository;
import com.boardgo.domain.review.repository.projection.ReviewCountProjection;
import com.boardgo.domain.review.repository.projection.ReviewMeetingReviewsProjection;
import com.boardgo.domain.review.service.response.ReviewMeetingParticipantsResponse;
import com.boardgo.domain.review.service.response.ReviewMeetingResponse;
import com.boardgo.domain.review.service.response.ReviewMeetingReviewsResponse;
import com.boardgo.domain.user.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;
    private final EvaluationTagRepository evaluationTagRepository;

    @Override
    public List<ReviewMeetingResponse> getReviewMeetings(ReviewType reviewType, Long userId) {
        switch (reviewType) {
            case PRE_PROGRESS -> {
                List<Long> reviewFinishedMeetings = findReviewFinishedList(userId);
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

        return List.of();
    }

    /***
     * 참여한 모임애서 참여자 모두에게 리뷰를 작성한 모임 찾기
     * @param userId
     * @return 리뷰 작성 완료 모임 ID 목록
     */
    private List<Long> findReviewFinishedList(Long userId) {
        List<ReviewCountProjection> reviewCountList =
                reviewRepository.countReviewByReviewerId(userId);

        Map<Long, Integer> reviewCountMap =
                reviewCountList.stream()
                        .collect(
                                Collectors.toMap(
                                        ReviewCountProjection::getMeetingId,
                                        ReviewCountProjection::getReviewCount));

        List<ParticipationCountProjection> participationCountList =
                meetingParticipantRepository.countMeetingParticipation(
                        reviewCountMap.keySet(), List.of(LEADER, PARTICIPANT));

        List<Long> reviewFinished = new ArrayList<>();
        for (ParticipationCountProjection participationCount : participationCountList) {
            Long meetingId = participationCount.getMeetingId();
            Integer reviewCount = reviewCountMap.get(meetingId);
            Integer participantCount = participationCount.getParticipationCount() - 1; // 본인 제외
            if (reviewCount == participantCount) {
                reviewFinished.add(meetingId);
            }
        }
        return reviewFinished;
    }

    @Override
    public void create(ReviewCreateRequest createRequest, Long reviewerId) {
        validateCreateReview(createRequest.meetingId(), createRequest.revieweeId(), reviewerId);
        ReviewEntity reviewEntity =
                reviewMapper.toReviewEntity(
                        createRequest, createRequest.evaluationTagList(), reviewerId);
        reviewRepository.save(reviewEntity);
    }

    private void validateCreateReview(Long meetingId, Long revieweeId, Long reviewerId) {
        MeetingEntity meetingEntity =
                meetingRepository
                        .findById(meetingId)
                        .orElseThrow(() -> new CustomNullPointException("모임이 존재하지 않습니다"));
        if (!meetingEntity.isFinishState()) {
            throw new CustomIllegalArgumentException("종료된 모임이 아닙니다");
        }
        if (!userRepository.existsById(revieweeId)) {
            throw new CustomNullPointException("회원이 존재하지 않습니다");
        }

        boolean written =
                reviewRepository.existsByReviewerIdAndMeetingIdAndRevieweeId(
                        reviewerId, meetingId, revieweeId);
        if (written) {
            throw new DuplicateException("이미 작성된 리뷰 입니다");
        }

        Long meetingParticipantCount =
                meetingParticipantRepository.countMeetingParticipant(
                        meetingId, List.of(revieweeId, reviewerId));
        final int TOGETHER = 2;
        if (meetingParticipantCount != TOGETHER) {
            throw new CustomIllegalArgumentException("모임을 함께 참여하지 않았습니다");
        }
    }

    @Override
    public List<ReviewMeetingParticipantsResponse> getReviewMeetingParticipants(
            Long meetingId, Long reviewerId) {
        List<Long> revieweeIds = reviewRepository.findAllRevieweeId(reviewerId, meetingId);
        revieweeIds.add(reviewerId); // 본인 리뷰 작성자 목록 표출 제외

        List<ReviewMeetingParticipantsProjection> reviewMeetingParticipants =
                meetingParticipantRepository.findReviewMeetingParticipants(revieweeIds, meetingId);
        List<ReviewMeetingParticipantsResponse> reviewMeetingParticipantsResponseList =
                reviewMapper.toReviewMeetingParticipantsList(reviewMeetingParticipants);
        if (reviewMeetingParticipantsResponseList.isEmpty()) {
            throw new CustomNoSuchElementException("리뷰를 작성할 참여자");
        }
        return reviewMeetingParticipantsResponseList;
    }

    @Override
    public List<ReviewMeetingReviewsResponse> getReviewMeetingReviews(
            Long meetingId, Long reviewerId) {
        // review 테이블에서 where meetingId, reviewerId + tag 테이블 조인해서 List 로 받아오기
        List<ReviewMeetingReviewsProjection> meetingReviews =
                reviewRepository.findMeetingReviews(meetingId, reviewerId);
        if (meetingReviews.isEmpty()) {
            throw new CustomNoSuchElementException("리뷰를 작성한 참여자");
        }

        List<ReviewMeetingReviewsResponse> reviewMeetingReviewsResponses = new ArrayList<>();

        List<List<String>> collect =
                meetingReviews.stream()
                        .map(ReviewMeetingReviewsProjection::evaluationTagIds)
                        .collect(Collectors.toList());
        int i = 0;
        for (ReviewMeetingReviewsProjection meetingReviewsProjection : meetingReviews) {
            List<Long> longs = stringToLongList(collect.get(i));
            List<EvaluationTagEntity> evaluationTagEntities =
                    evaluationTagRepository.findAllById(longs);

            List<String> positiveTags = new ArrayList<>();
            List<String> negativeTags = new ArrayList<>();
            for (EvaluationTagEntity evaluationTag : evaluationTagEntities) {
                switch (evaluationTag.getEvaluationType()) {
                    case POSITIVE -> positiveTags.add(evaluationTag.getTagPhrase());
                    case NEGATIVE -> negativeTags.add(evaluationTag.getTagPhrase());
                }
            }

            i++;
            ReviewMeetingReviewsResponse reviewMeetingReviewsResponse =
                    reviewMapper.toReviewMeetingReviewsResponse(
                            meetingReviewsProjection, positiveTags, negativeTags);
            reviewMeetingReviewsResponses.add(reviewMeetingReviewsResponse);
        }

        return reviewMeetingReviewsResponses;
    }
}
