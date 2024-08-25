package com.boardgo.domain.review.service.response;

import java.time.LocalDateTime;

public record ReviewMeetingResponse(
        Long meetingId,
        String title,
        String thumbnail,
        String city,
        String country,
        LocalDateTime meetingDate) {}
