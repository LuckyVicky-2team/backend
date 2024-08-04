package com.boardgo.domain.meeting.service;

import com.boardgo.domain.meeting.controller.dto.MeetingCreateRequest;

public interface MeetingUseCase {
	Long create(MeetingCreateRequest meetingCreateRequest);
}
