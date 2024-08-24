package com.boardgo.domain.meeting.service.response;

import java.time.LocalDateTime;

public record MeetingMyPageResponse(
        Long id,
        String title,
        String thumbnail,
        String detailAddress,
        LocalDateTime meetingDatetime,
        Integer limitParticipant,
        Integer currentParticipant) {}
