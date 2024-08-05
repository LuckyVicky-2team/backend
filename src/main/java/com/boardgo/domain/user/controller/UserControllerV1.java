package com.boardgo.domain.user.controller;

import static com.boardgo.common.constant.HeaderConstant.*;

import com.boardgo.domain.user.controller.dto.EmailRequest;
import com.boardgo.domain.user.service.UserQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerV1 {
    private final UserQueryUseCase userQueryUseCase;

    @GetMapping(value = "/check-email", headers = API_VERSION_HEADER1)
    public ResponseEntity<Void> checkEmail(EmailRequest emailRequest) {
        if (userQueryUseCase.existEmail(emailRequest)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
