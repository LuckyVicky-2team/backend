package com.boardgo.domain.meeting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boardgo.domain.meeting.controller.dto.MeetingCreateRequest;
import com.boardgo.domain.meeting.repository.MeetingRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingCommandServiceV1 implements MeetingUseCase {
	private final MeetingRepository meetingRepository;

	@Override
	public Long create(MeetingCreateRequest meetingCreateRequest) {
		return null;
	}
}
