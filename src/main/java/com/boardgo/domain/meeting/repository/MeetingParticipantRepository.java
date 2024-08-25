package com.boardgo.domain.meeting.repository;

import com.boardgo.domain.meeting.entity.MeetingParticipantEntity;
import com.boardgo.domain.meeting.entity.ParticipantType;
import com.boardgo.domain.meeting.repository.projection.ParticipationCountProjection;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingParticipantRepository
        extends JpaRepository<MeetingParticipantEntity, Long> {
    List<MeetingParticipantEntity> findByMeetingId(Long meetingId);

    @Query(
            "SELECT COUNT(*) FROM MeetingParticipantEntity ep WHERE ep.type IN (:types) AND ep.userInfoId = :userId")
    Integer countByTypeAndUserInfoId(
            @Param("types") List<String> types, @Param("userId") Long userId);

    // FIXME List<ParticipantType> types 수정

    boolean existsByUserInfoIdAndMeetingId(Long userId, Long meetingId);

    @Query(
            "SELECT mp.meetingId, COUNT(*) participationCount "
                    + "FROM MeetingParticipantEntity mp "
                    + "WHERE mp.meetingId IN (:meetingIds) AND mp.type IN (:types) "
                    + "GROUP BY mp.meetingId")
    List<ParticipationCountProjection> countMeetingParticipation(
            @Param("meetingIds") Set<Long> meetingIds, @Param("types") List<ParticipantType> types);
}
