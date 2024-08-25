package com.boardgo.domain.review.controller;

import static com.boardgo.common.constant.HeaderConstant.API_VERSION_HEADER1;
import static com.boardgo.common.utils.SecurityUtils.currentUserId;

import com.boardgo.domain.review.entity.enums.ReviewType;
import com.boardgo.domain.review.service.ReviewUseCase;
import com.boardgo.domain.review.service.response.ReviewMeetingsResponse;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewUseCase reviewUseCase;

    @GetMapping(value = "/meetings", headers = API_VERSION_HEADER1)
    public ResponseEntity<List<ReviewMeetingsResponse>> getReviewMeetings(
            @RequestParam("reviewType") @NotNull ReviewType reviewType) {
        return ResponseEntity.ok(reviewUseCase.getReviewMeetings(reviewType, currentUserId()));
    }
}
