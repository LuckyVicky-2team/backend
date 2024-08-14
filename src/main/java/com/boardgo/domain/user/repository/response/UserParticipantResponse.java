package com.boardgo.domain.user.repository.response;

public record UserParticipantResponse(
        Long userId, String profileImage, String nickname, String roleType) {}
