package com.boardgo.integration.fixture;

import com.boardgo.domain.meeting.entity.MeetingParticipantEntity;
import com.boardgo.domain.meeting.entity.ParticipantType;

public abstract class MeetingParticipantFixture {

    public static MeetingParticipantEntity getLeaderProgressMeetingEntity(
            Long meetingId, Long userId) {
        return MeetingParticipantEntity.builder()
                .meetingId(meetingId)
                .userInfoId(userId)
                .type(ParticipantType.LEADER)
                .build();
    }

    public static MeetingParticipantEntity getParticipantMeetingParticipantEntity(
            Long meetingId, Long userId) {
        return MeetingParticipantEntity.builder()
                .meetingId(meetingId)
                .userInfoId(userId)
                .type(ParticipantType.PARTICIPANT)
                .build();
    }
}
