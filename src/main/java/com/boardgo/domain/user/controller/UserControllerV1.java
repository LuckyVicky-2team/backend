package com.boardgo.domain.user.controller;

import static com.boardgo.common.constant.HeaderConstant.API_VERSION_HEADER1;
import static com.boardgo.common.utils.SecurityUtils.currentUserId;

import com.boardgo.domain.user.controller.dto.EmailRequest;
import com.boardgo.domain.user.controller.dto.NickNameRequest;
import com.boardgo.domain.user.controller.dto.UserPersonalInfoResponse;
import com.boardgo.domain.user.controller.dto.UserPersonalInfoUpdateRequest;
import com.boardgo.domain.user.service.UserCommandUseCase;
import com.boardgo.domain.user.service.UserQueryUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class UserControllerV1 {
    private final UserQueryUseCase userQueryUseCase;
    private final UserCommandUseCase userCommandUseCase;

    @GetMapping(value = "/check-email", headers = API_VERSION_HEADER1)
    public ResponseEntity<Void> checkEmail(@Valid EmailRequest emailRequest) {
        userQueryUseCase.existEmail(emailRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/check-nickname", headers = API_VERSION_HEADER1)
    public ResponseEntity<Void> checkNickName(@Valid NickNameRequest nickNameRequest) {
        userQueryUseCase.existNickName(nickNameRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/me", headers = API_VERSION_HEADER1)
    public ResponseEntity<String> me() {
        return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping(value = "/personal-info", headers = API_VERSION_HEADER1)
    public ResponseEntity<UserPersonalInfoResponse> getPersonalInfo() {
        UserPersonalInfoResponse personalInfo = userQueryUseCase.getPersonalInfo(currentUserId());
        return ResponseEntity.ok(personalInfo);
    }

    @PutMapping(value = "/personal-info", headers = API_VERSION_HEADER1)
    public ResponseEntity<Void> updatePersonalInfo(
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestPart @Valid UserPersonalInfoUpdateRequest updateRequest) {
        userCommandUseCase.updatePersonalInfo(currentUserId(), updateRequest, profileImage);
        return ResponseEntity.ok().build();
    }
}
