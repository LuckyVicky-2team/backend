package com.boardgo.domain.review.service;

import com.boardgo.domain.review.controller.dto.EvaluationTagsResponse;
import com.boardgo.domain.review.repository.EvaluationTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EvaluationTagServiceV1 implements EvaluationTagUseCase {

    private final EvaluationTagRepository evaluationTagRepository;

    @Override
    public EvaluationTagsResponse getEvaluationTags() {
        return null;
    }
}
