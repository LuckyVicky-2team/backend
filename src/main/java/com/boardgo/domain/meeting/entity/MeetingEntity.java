package com.boardgo.domain.meeting.entity;

import java.time.LocalDateTime;

import com.boardgo.common.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

	@Column(nullable = false)
	private MeetingType type;

	@Column
	private String thumbnail;

	@Column
	private String city;

	@Column
	private String county;

	@Column(name = "meeting_datetime", nullable = false, columnDefinition = "DATETIME")
	private LocalDateTime meetingDatetime;

	@Column(name = "end_datetime", nullable = false, columnDefinition = "DATETIME")
	private LocalDateTime endDatetime;

	@Builder
	private MeetingEntity(Long id, String content, MeetingType type, String thumbnail, String city, String county,
		LocalDateTime meetingDatetime, LocalDateTime endDatetime) {
		this.id = id;
		this.content = content;
		this.type = type;
		this.thumbnail = thumbnail;
		this.city = city;
		this.county = county;
		this.meetingDatetime = meetingDatetime;
		this.endDatetime = endDatetime;
	}
}
