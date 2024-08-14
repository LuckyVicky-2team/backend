package com.boardgo.domain.user.repository.projection;

public record UserParticipantProjection(
        Long userId, String profileImage, String nickname, String roleType) {}
