package com.boardgo.domain.review.service;

import com.boardgo.domain.review.entity.enums.ReviewType;
import com.boardgo.domain.review.service.response.ReviewMeetingResponse;
import java.util.List;

public interface ReviewUseCase {

    List<ReviewMeetingResponse> getReviewMeetings(ReviewType reviewType, Long userId);
}
