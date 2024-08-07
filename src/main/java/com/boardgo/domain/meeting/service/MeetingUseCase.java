package com.boardgo.domain.meeting.service;

import com.boardgo.domain.meeting.controller.dto.MeetingCreateRequest;
import org.springframework.web.multipart.MultipartFile;

public interface MeetingUseCase {
    Long create(MeetingCreateRequest meetingCreateRequest, MultipartFile imageFile);
}
