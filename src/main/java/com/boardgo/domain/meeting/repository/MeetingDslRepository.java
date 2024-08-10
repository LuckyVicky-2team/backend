package com.boardgo.domain.meeting.repository;

import com.boardgo.domain.meeting.controller.dto.MeetingSearchRequest;
import com.boardgo.domain.meeting.repository.dto.MeetingSearchDto;
import org.springframework.data.domain.Page;

public interface MeetingDslRepository {
    Page<MeetingSearchDto> findByFilters(MeetingSearchRequest searchRequest);
}
