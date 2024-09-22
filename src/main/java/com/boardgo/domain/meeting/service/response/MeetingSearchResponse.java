package com.boardgo.domain.meeting.service.response;

import java.time.LocalDateTime;

public record MeetingSearchResponse(
        Long id,
        String title,
        String city,
        String county,
        String thumbnail,
        Long viewCount,
        LocalDateTime meetingDate,
        Integer limitParticipant,
        String nickName,
        String genres,
        Integer participantCount) {}
