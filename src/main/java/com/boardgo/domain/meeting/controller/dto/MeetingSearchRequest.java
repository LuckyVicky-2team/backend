package com.boardgo.domain.meeting.controller.dto;

import com.boardgo.common.validator.annotation.AllowedValues;
import java.time.LocalDateTime;

public record MeetingSearchRequest(
        Long count,
        String genre,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String searchWord,
        @AllowedValues(values = {"TITLE", "CONTENT", "ALL"}) String searchType,
        String city,
        String county,
        int page,
        int size,
        @AllowedValues(values = {"MEETING_DATE", "PARTICIPANT_COUNT"}) String sortBy) {}
