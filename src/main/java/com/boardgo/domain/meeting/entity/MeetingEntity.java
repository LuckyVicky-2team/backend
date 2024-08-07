package com.boardgo.domain.meeting.entity;

import com.boardgo.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "meeting")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingEntity extends BaseEntity {
    @Id
    @Column(name = "meeting_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = false)
    private MeetingType type;

    @Column(name = "limit_participant", columnDefinition = "SMALLINT")
    private Integer limitParticipant;

    @Column private String thumbnail;

    @Column private String city;

    @Column private String county;

    @Column(length = 64)
    private String latitude;

    @Column(length = 64)
    private String longitude;

    @Column(name = "meeting_datetime", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime meetingDatetime;

    @Builder
    private MeetingEntity(
            Long id,
            String content,
            MeetingType type,
            Integer limitParticipant,
            String thumbnail,
            String city,
            String county,
            LocalDateTime meetingDatetime) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.limitParticipant = limitParticipant;
        this.thumbnail = thumbnail;
        this.city = city;
        this.county = county;
        this.meetingDatetime = meetingDatetime;
    }
}
