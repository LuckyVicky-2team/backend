package com.boardgo.domain.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boardgo.domain.meeting.entity.MeetingEntity;

public interface MeetingRepository extends JpaRepository<MeetingEntity, Long> {
}
