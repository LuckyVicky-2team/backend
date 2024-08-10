package com.boardgo.domain.user.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record SocialSignupRequest(
        @NotEmpty(message = "nickName") String nickName, List<String> prTags) {}
