package com.boardgo.domain.meeting.service;

import com.boardgo.common.exception.CustomNullPointException;
import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.entity.enums.MeetingState;
import com.boardgo.domain.meeting.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingCommandServiceV1 implements MeetingCommandUseCase {
    private final MeetingRepository meetingRepository;

    @Override
    public Long create(MeetingEntity meeting) {
        return meetingRepository.save(meeting).getId();
    }

    @Override
    public void deleteById(Long meetingId) {
        meetingRepository.deleteById(meetingId);
    }

    @Override
    public void incrementShareCount(Long meetingId) {
        MeetingEntity meeting = getMeeting(meetingId);
        meeting.incrementShareCount();
    }

    @Override
    public void incrementViewCount(Long meetingId) {
        MeetingEntity meeting = getMeeting(meetingId);
        meeting.incrementViewCount();
    }

    @Override
    public void updateMeetingState(Long meetingId, MeetingState state) {
        MeetingEntity meeting = getMeeting(meetingId);
        meeting.updateMeetingState(state);
    }

    private MeetingEntity getMeeting(Long meetingId) {
        return meetingRepository
                .findById(meetingId)
                .orElseThrow(() -> new CustomNullPointException("모임이 존재하지 않습니다"));
    }
}
