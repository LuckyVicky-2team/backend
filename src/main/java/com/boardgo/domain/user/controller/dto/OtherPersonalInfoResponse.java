package com.boardgo.domain.user.controller.dto;

import java.util.List;

public record OtherPersonalInfoResponse(
        String nickName,
        String profileImage,
        Double averageRating,
        List<String> prTags,
        int meetingCount) {}
