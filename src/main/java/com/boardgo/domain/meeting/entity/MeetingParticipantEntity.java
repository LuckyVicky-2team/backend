package com.boardgo.domain.meeting.entity;

import com.boardgo.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "meeting_participant",
        indexes = {
            @Index(name = "idx_user_info_id_type", columnList = "user_info_id,type"),
            @Index(name = "idx_meeting_id", columnList = "meeting_id")
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingParticipantEntity extends BaseEntity {
    @Id
    @Column(name = "meeting_participant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "meeting_id")
    private Long meetingId;

    @Column(name = "user_info_id")
    private Long userInfoId;

    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = false)
    private ParticipantType type;

    @Builder
    private MeetingParticipantEntity(
            Long id, Long meetingId, Long userInfoId, ParticipantType type) {
        this.id = id;
        this.meetingId = meetingId;
        this.userInfoId = userInfoId;
        this.type = type;
    }
}
