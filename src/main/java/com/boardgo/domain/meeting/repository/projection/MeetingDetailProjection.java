package com.boardgo.domain.meeting.repository.projection;

import com.boardgo.domain.meeting.entity.MeetingState;

public record MeetingDetailProjection(
        Long meetingId,
        String title,
        String content,
        String longitude,
        String latitude,
        String city,
        String county,
        Integer limitParticipant,
        MeetingState state
        // TODO: 찜 여부 추가
        ) {}
