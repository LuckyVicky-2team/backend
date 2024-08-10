package com.boardgo.domain.meeting.repository;

import com.boardgo.domain.meeting.entity.MeetingParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingParticipantRepository
        extends JpaRepository<MeetingParticipantEntity, Long> {}
