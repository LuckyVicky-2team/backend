package com.boardgo.domain.meeting.repository.projection;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record HomeMeetingDeadlineProjection(
        Long meetingId,
        String thumbnail,
        String title,
        String city,
        String county,
        LocalDateTime meetingDatetime) {
    @QueryProjection
    public HomeMeetingDeadlineProjection {}
}
