package com.boardgo.domain.meeting.repository.dto;

import java.time.LocalDateTime;
import java.util.List;

public record MeetingSearchDto(
        Long id,
        String title,
        String city,
        String county,
        LocalDateTime meetingDate,
        Integer limitParticipant,
        String nickName,
        List<String> games,
        List<String> genres,
        Long participantCount) {}
