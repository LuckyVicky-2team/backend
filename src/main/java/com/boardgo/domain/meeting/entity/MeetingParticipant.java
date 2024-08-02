package com.boardgo.domain.meeting.entity;

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
public class MeetingParticipant extends BaseEntity {
	@Id
	@Column(name = "meeting_participant_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "meeting_id")
	private Long meetingId;

	@Column(name = "user_info_id")
	private Long userInfoId;

	@Column(length = 32)
	private ParticipantType type;

	@Builder
	public MeetingParticipant(Long id, Long meetingId, Long userInfoId, ParticipantType type) {
		this.id = id;
		this.meetingId = meetingId;
		this.userInfoId = userInfoId;
		this.type = type;
	}
}
