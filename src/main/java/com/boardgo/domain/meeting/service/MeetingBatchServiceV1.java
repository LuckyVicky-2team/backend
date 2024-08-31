package com.boardgo.domain.meeting.service;

import static com.boardgo.domain.meeting.entity.enums.MeetingState.COMPLETE;
import static com.boardgo.domain.meeting.entity.enums.MeetingState.FINISH;
import static com.boardgo.domain.meeting.entity.enums.MeetingState.PROGRESS;

import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.repository.MeetingRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class MeetingBatchServiceV1 {
    private final MeetingRepository meetingRepository;

    public void updateFinishMeetingState() {
        List<MeetingEntity> meetingEntities =
                meetingRepository.findAllByMeetingDatetimeBefore(LocalDateTime.now());
        meetingEntities.forEach((meetingEntity -> meetingEntity.updateMeetingState(FINISH)));
    }

    public void updateCompleteMeetingState() {
        List<Long> meetingIds = meetingRepository.findCompleteMeetingId(PROGRESS);
        List<MeetingEntity> meetingEntities = meetingRepository.findByIdIn(meetingIds);
        meetingEntities.forEach((meetingEntity -> meetingEntity.updateMeetingState(COMPLETE)));
    }
}
