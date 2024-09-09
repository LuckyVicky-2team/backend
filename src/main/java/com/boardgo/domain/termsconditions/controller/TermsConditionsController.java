package com.boardgo.domain.termsconditions.controller;

import static com.boardgo.common.utils.SecurityUtils.currentUserId;

import com.boardgo.domain.termsconditions.controller.request.TermsConditionsCreateRequest;
import com.boardgo.domain.termsconditions.service.facade.UserTermsConditionsCommandFacade;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/terms-conditions")
public class TermsConditionsController {

    private final UserTermsConditionsCommandFacade userTermsConditionsCommandFacade;

    @PostMapping("/user")
    public ResponseEntity<Void> createUserTermsConditions(
            @RequestBody @Valid List<TermsConditionsCreateRequest> request) {
        userTermsConditionsCommandFacade.createUserTermsConditions(request, currentUserId());
        return ResponseEntity.ok().build();
    }

    // TODO. 약관 추가 및 버전업데이트 API
}
