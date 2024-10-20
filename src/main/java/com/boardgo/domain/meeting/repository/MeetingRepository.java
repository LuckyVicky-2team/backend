package com.boardgo.domain.meeting.repository;

import com.boardgo.domain.meeting.entity.MeetingEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository
        extends JpaRepository<MeetingEntity, Long>, MeetingDslRepository {
    List<MeetingEntity> findByIdIn(List<Long> meetingIdList);

    List<MeetingEntity> findAllByMeetingDatetimeBefore(LocalDateTime now);
}
