package com.boardgo.domain.meeting.repository.projection;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record MeetingReviewProjection(
        Long meetingId,
        String title,
        String thumbnail,
        String city,
        String country,
        LocalDateTime meetingDate) {
    @QueryProjection
    public MeetingReviewProjection {}
}
