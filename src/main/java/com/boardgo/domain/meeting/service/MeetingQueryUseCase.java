package com.boardgo.domain.meeting.service;

import com.boardgo.domain.meeting.controller.request.MeetingSearchRequest;
import com.boardgo.domain.meeting.repository.response.MeetingSearchResponse;
import org.springframework.data.domain.Page;

public interface MeetingQueryUseCase {

    Page<MeetingSearchResponse> search(MeetingSearchRequest meetingSearchRequest);
}