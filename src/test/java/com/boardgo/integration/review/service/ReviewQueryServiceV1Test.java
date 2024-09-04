package com.boardgo.integration.review.service;

import com.boardgo.domain.review.service.ReviewQueryUseCase;
import com.boardgo.integration.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ReviewQueryServiceV1Test extends IntegrationTestSupport {

    @Autowired private ReviewQueryUseCase reviewQueryUseCase;

    @Test
    @DisplayName("강퇴된 모임은 리뷰 모임 목록에 존재할 수 없다")
    void 강퇴된_모임은_리뷰_모임_목록에_존재할_수_없다() {
        // given

        // when

        // then

    }

    // TODO 종료된 모임만 리뷰 모임 목록에 존재한다
    // TODO 모임에 참가한 참여자에게 모두 리뷰를 작성했을 경우 작성할 리뷰 모임 목록에 존재하지 않는다

    // TODO 리뷰 작성 시 종료된 모임이 아니면 예외가 발생한다
    // TODO 리뷰 작성 시 이미 작성된 리뷰일 경우 예외갸 발생한다
    // TODO 리뷰 작성 시 모임에 함께 참여한 참여자가 아닌 경우 예외가 발생한다.
}
