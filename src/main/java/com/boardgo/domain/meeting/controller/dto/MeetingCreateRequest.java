package com.boardgo.domain.meeting.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record MeetingCreateRequest(
	@NotEmpty(message = "content") String content,
	@NotEmpty(message = "type") String type,
	String thumbnail,
	String city,
	String county,
	@NotNull @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime meetingDatetime,
	@NotNull @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime endDatetime,
	List<Long> boardGameIdList
) {
}
