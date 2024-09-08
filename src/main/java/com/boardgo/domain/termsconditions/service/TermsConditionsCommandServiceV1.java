package com.boardgo.domain.termsconditions.service;

import com.boardgo.domain.termsconditions.controller.request.TermsConditionsCreateRequest;
import com.boardgo.domain.termsconditions.repository.TermsConditionsRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TermsConditionsCommandServiceV1 implements TermsConditionsCommandUseCase {
    private final TermsConditionsRepository termsConditionsRepository;

    @Override
    public void create(
            List<TermsConditionsCreateRequest> termsConditionsCreateRequest, Long userId) {}
}
