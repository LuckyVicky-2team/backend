package com.boardgo.domain.meeting.controller.dto;

import com.boardgo.common.validator.annotation.AllowedValues;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

public record MeetingCreateRequest(
        @NotEmpty(message = "content") String content,
        @NotEmpty(message = "type") @AllowedValues(values = {"Free", "ACCEPT"}) String type,
        @Positive Integer limitParticipant,
        String city,
        String county,
        @Future @NotNull @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime meetingDatetime,
        @Future @NotNull @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDatetime,
        List<Long> boardGameIdList) {}
