package com.boardgo.domain.review.repository;

import com.boardgo.domain.review.repository.projection.ReviewCountProjection;

public interface ReviewDslRepository {

    ReviewCountProjection countReviewByReviewerId(Long reviewerId);
}
