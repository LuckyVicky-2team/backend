package com.boardgo.domain.meeting.repository;

import com.boardgo.domain.meeting.entity.MeetingGenreMatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingGenreMatchRepository
        extends JpaRepository<MeetingGenreMatchEntity, Long>, MeetingGenreMatchJdbcRepository {}
