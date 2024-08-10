package com.boardgo.domain.meeting.repository.dto;

import com.boardgo.domain.meeting.entity.MeetingState;
import java.util.List;

public record MeetingSearchDto(
        Long id,
        String title,
        String nickName,
        String city,
        String county,
        List<String> genres,
        Integer limitParticipant,
        MeetingState state) {}
