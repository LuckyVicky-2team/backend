package com.boardgo.domain.termsconditions.service;

import com.boardgo.domain.termsconditions.controller.request.TermsConditionsCreateRequest;
import java.util.List;

public interface TermsConditionsCommandUseCase {
    void create(List<TermsConditionsCreateRequest> termsConditionsCreateRequest, Long userId);
}
