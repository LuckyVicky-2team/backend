package com.boardgo.domain.meeting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

@Entity
@Immutable
@Subselect(
        """
	SELECT m.meeting_id AS id, COUNT(mp.meeting_participant_id) AS participant_count
	FROM meeting m
	INNER JOIN meeting_participant mp ON m.meeting_id = mp.meeting_id
	WHERE mp.type != 'OUT'
	GROUP BY m.meeting_id
""")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingParticipantSubEntity {
    @Id private Long id;

    @Column(name = "participantCount")
    private Long participantCount;
}
