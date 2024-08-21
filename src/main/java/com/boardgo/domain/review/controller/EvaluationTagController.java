package com.boardgo.domain.review.controller;

import com.boardgo.domain.review.controller.dto.EvaluationTagsResponse;
import com.boardgo.domain.review.service.EvaluationTagUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EvaluationTagController {

    private final EvaluationTagUseCase evaluationTagUseCase;

    @GetMapping("/evaluationTags")
    public ResponseEntity<EvaluationTagsResponse> getEvaluationTags() {
        return ResponseEntity.ok(evaluationTagUseCase.getEvaluationTags());
    }
}
