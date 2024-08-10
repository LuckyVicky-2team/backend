package com.boardgo.domain.meeting.service;

import com.boardgo.domain.meeting.controller.dto.MeetingSearchRequest;
import com.boardgo.domain.meeting.repository.MeetingRepository;
import com.boardgo.domain.meeting.repository.dto.MeetingSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingQueryServiceV1 implements MeetingQueryUsecase {
    private final MeetingRepository meetingRepository;

    public Page<MeetingSearchDto> search(MeetingSearchRequest meetingSearchRequest) {
        return meetingRepository.findByFilters(meetingSearchRequest);
    }
}
