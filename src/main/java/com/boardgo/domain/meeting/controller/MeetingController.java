package com.boardgo.domain.meeting.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boardgo.domain.meeting.controller.dto.MeetingCreateRequest;
import com.boardgo.domain.meeting.service.MeetingUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MeetingController {
	private final MeetingUseCase meetingUseCase;

	// TODO: HeaderConstant Merge 시에 Header API 버전 추가
	@PostMapping(value = "/meeting")
	public ResponseEntity<Void> create(MeetingCreateRequest meetingCreateRequest) {
		Long meetingId = meetingUseCase.create(meetingCreateRequest);
		return ResponseEntity
			.created(URI.create(String.valueOf(meetingId)))
			.build();

	}
}
