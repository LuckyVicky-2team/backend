package com.boardgo.domain.review.service;

import com.boardgo.domain.review.entity.enums.ReviewType;
import com.boardgo.domain.review.service.response.ReviewMeetingsResponse;
import java.util.List;

public interface ReviewUseCase {

    List<ReviewMeetingsResponse> getReviewMeetings(ReviewType reviewType, Long userId);
}
