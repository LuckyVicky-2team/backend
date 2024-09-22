package com.boardgo.domain.meeting.service.response;

import com.boardgo.domain.meeting.entity.enums.MeetingState;
import java.time.LocalDateTime;

public record MeetingDetailResponse(
        Long meetingId,
        String userNickName,
        Long userId,
        LocalDateTime meetingDatetime,
        String thumbnail,
        String title,
        String content,
        String longitude,
        String latitude,
        String city,
        String county,
        String locationName,
        String detailAddress,
        Integer limitParticipant,
        MeetingState state,
        Integer shareCount,
        Long viewCount) {}
