package com.boardgo.domain.review.controller;

import static com.boardgo.common.constant.HeaderConstant.API_VERSION_HEADER1;
import static com.boardgo.common.utils.SecurityUtils.currentUserId;

import com.boardgo.domain.review.controller.request.ReviewCreateRequest;
import com.boardgo.domain.review.entity.enums.ReviewType;
import com.boardgo.domain.review.service.ReviewUseCase;
import com.boardgo.domain.review.service.response.ReviewMeetingResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewUseCase reviewUseCase;

    @GetMapping(value = "/meetings", headers = API_VERSION_HEADER1)
    public ResponseEntity<List<ReviewMeetingResponse>> getReviewMeetings(
            @RequestParam("reviewType") @Valid @NotNull ReviewType reviewType) {
        return ResponseEntity.ok(reviewUseCase.getReviewMeetings(reviewType, currentUserId()));
    }

    @PostMapping(value = "", headers = API_VERSION_HEADER1)
    public ResponseEntity<Void> create(@RequestBody @Valid ReviewCreateRequest createRequest) {
        reviewUseCase.create(createRequest, currentUserId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
