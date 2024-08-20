package com.boardgo.domain.user.controller;

import static com.boardgo.common.constant.HeaderConstant.API_VERSION_HEADER1;
import static com.boardgo.common.utils.SecurityUtils.currentUserId;

import com.boardgo.domain.user.controller.dto.OtherPersonalInfoResponse;
import com.boardgo.domain.user.controller.dto.UserPersonalInfoResponse;
import com.boardgo.domain.user.controller.dto.UserPersonalInfoUpdateRequest;
import com.boardgo.domain.user.service.UserCommandUseCase;
import com.boardgo.domain.user.service.UserQueryUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/personal-info")
public class PersonalInfoController {
    private final UserQueryUseCase userQueryUseCase;
    private final UserCommandUseCase userCommandUseCase;

    @GetMapping(value = "", headers = API_VERSION_HEADER1)
    public ResponseEntity<UserPersonalInfoResponse> getPersonalInfo() {
        return ResponseEntity.ok(userQueryUseCase.getPersonalInfo(currentUserId()));
    }

    @GetMapping(value = "/{userId}", headers = API_VERSION_HEADER1)
    public ResponseEntity<OtherPersonalInfoResponse> getOtherPersonalInfo(
            @PathVariable("userId") @Positive @NotNull long userId) {
        return ResponseEntity.ok(userQueryUseCase.getOtherPersonalInfo(userId));
    }

    @PatchMapping(value = "", headers = API_VERSION_HEADER1)
    public ResponseEntity<Void> updatePersonalInfo(
            @RequestBody UserPersonalInfoUpdateRequest updateRequest) {
        userCommandUseCase.updatePersonalInfo(currentUserId(), updateRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/profile", headers = API_VERSION_HEADER1)
    public ResponseEntity<Void> updateProfileImage(
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        userCommandUseCase.updateProfileImage(currentUserId(), profileImage);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/prTags", headers = API_VERSION_HEADER1)
    public ResponseEntity<Void> updatePrTags(@RequestParam("prTags") @Valid List<String> prTags) {
        userCommandUseCase.updatePrTags(prTags, currentUserId());
        return ResponseEntity.ok().build();
    }
}
