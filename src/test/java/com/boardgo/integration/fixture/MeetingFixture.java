package com.boardgo.integration.fixture;

import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.entity.MeetingState;
import com.boardgo.domain.meeting.entity.MeetingType;
import java.time.LocalDateTime;

public abstract class MeetingFixture {

    public static MeetingEntity getProgressMeetingEntity(
            Long userId, MeetingType type, int limitParticipant) {
        return MeetingEntity.builder()
                .userId(userId)
                .title("보드게임 같이 하실분 구해요!")
                .content("서울대입구역 4번출구에서 만나요")
                .type(type)
                .limitParticipant(limitParticipant)
                .thumbnail("보드게임.jpg")
                .city("서울시")
                .county("관악구")
                .latitude("51.5429")
                .longitude("434.5785")
                .meetingDatetime(LocalDateTime.now().plusDays(5))
                .state(MeetingState.PROGRESS)
                .build();
    }
}
