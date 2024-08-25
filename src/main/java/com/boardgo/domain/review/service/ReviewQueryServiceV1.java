package com.boardgo.domain.review.service;

import com.boardgo.domain.mapper.ReviewMapper;
import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.repository.MeetingRepository;
import com.boardgo.domain.review.entity.enums.ReviewType;
import com.boardgo.domain.review.repository.ReviewRepository;
import com.boardgo.domain.review.service.response.ReviewMeetingsResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewQueryServiceV1 implements ReviewUseCase {

    private final ReviewRepository reviewRepository;
    private final MeetingRepository meetingRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public List<ReviewMeetingsResponse> getReviewMeetings(ReviewType reviewType, Long userId) {
        switch (reviewType) {
            case PRE_PROGRESS -> {
                // 리뷰 작성완료 기준: review 테이블에 where userId / 어떤 모임Id가 있는데,
                // 해당 모임의 리뷰갯수가 3명 + 모임참여자 수가 5명(실질적으로 나 제외 4명),
                // 리뷰갯수, 모임참여자수, 모임Id
                // 리뷰갯수 == 모임참여자수 -> 리뷰 작성완료

                // meetingParticipate 에서 userId 가 Leader 또는 참여자로 참여한 모임 + 종료된 모임 조회
                // + where 위의 meetingId 는 제외하고 조회

            }
            case FINISH -> {
                List<Long> meetingIds = reviewRepository.findFinishedReview(userId);
                List<MeetingEntity> meetingEntityList = meetingRepository.findAllById(meetingIds);
                return reviewMapper.toReviewMeetingsResponseList(meetingEntityList);
            }
        }
        return null;
    }
}
