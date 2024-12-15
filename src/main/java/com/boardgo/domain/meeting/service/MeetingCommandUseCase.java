package com.boardgo.domain.meeting.service;

import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.entity.enums.MeetingState;

public interface MeetingCommandUseCase {
    Long create(MeetingEntity meeting);

    void deleteById(Long meetingId);

    void incrementShareCount(Long meetingId);

    void incrementViewCount(Long meetingId);

    void updateMeetingState(Long meetingId, MeetingState state);
}
