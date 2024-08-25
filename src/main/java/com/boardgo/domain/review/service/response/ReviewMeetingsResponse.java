package com.boardgo.domain.review.service.response;

import java.time.LocalDateTime;

public record ReviewMeetingsResponse(
        Long meetingId,
        String title,
        String thumbnail,
        String city,
        String country,
        LocalDateTime meetingDate) {}
