package com.boardgo.integration.meeting.service;

import static com.boardgo.domain.meeting.entity.enums.MeetingState.COMPLETE;
import static com.boardgo.integration.data.MeetingData.getMeetingEntityData;
import static com.boardgo.integration.data.UserInfoData.userInfoEntityData;
import static com.boardgo.integration.fixture.MeetingParticipantFixture.getLeaderMeetingParticipantEntity;
import static com.boardgo.integration.fixture.MeetingParticipantFixture.getParticipantMeetingParticipantEntity;
import static org.assertj.core.api.Assertions.assertThat;

import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.entity.MeetingParticipantEntity;
import com.boardgo.domain.meeting.entity.enums.MeetingState;
import com.boardgo.domain.meeting.entity.enums.MeetingType;
import com.boardgo.domain.meeting.entity.enums.ParticipantType;
import com.boardgo.domain.meeting.repository.MeetingParticipantRepository;
import com.boardgo.domain.meeting.repository.MeetingRepository;
import com.boardgo.domain.meeting.service.MeetingCommandServiceV1;
import com.boardgo.domain.user.repository.UserRepository;
import com.boardgo.integration.support.IntegrationTestSupport;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MeetingCommandServiceV1Test extends IntegrationTestSupport {
    @Autowired private MeetingCommandServiceV1 meetingCommandServiceV1;
    @Autowired private MeetingRepository meetingRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private MeetingParticipantRepository meetingParticipantRepository;
    @Autowired private EntityManager entityManager;

    @Test
    @DisplayName("모임 상세 조회 시 조회수가 오른다")
    void 모임_상세_조회_시_조회수가_오른다() {
        // given
        LocalDateTime meetingDatetime = LocalDateTime.now().plusDays(1);
        MeetingEntity meetingEntity =
                MeetingEntity.builder()
                        .viewCount(0L)
                        .userId(1L)
                        .latitude("12312312")
                        .longitude("12321")
                        .thumbnail("thumbnail")
                        .state(COMPLETE)
                        .meetingDatetime(meetingDatetime)
                        .type(MeetingType.FREE)
                        .content("content")
                        .city("city")
                        .county("county")
                        .title("title")
                        .locationName("location")
                        .detailAddress("detailAddress")
                        .limitParticipant(5)
                        .build();

        MeetingEntity savedMeeting = meetingRepository.save(meetingEntity);
        Long meetingId = savedMeeting.getId();
        meetingParticipantRepository.save(
                MeetingParticipantEntity.builder()
                        .userInfoId(1L)
                        .meetingId(meetingId)
                        .type(ParticipantType.LEADER)
                        .build());

        // when
        // then
        meetingCommandServiceV1.incrementViewCount(meetingId);
        MeetingEntity result = meetingRepository.findById(meetingId).get();
        assertThat(result.getViewCount()).isEqualTo(1L);

        meetingCommandServiceV1.incrementViewCount(meetingId);
        MeetingEntity result2 = meetingRepository.findById(meetingId).get();
        assertThat(result2.getViewCount()).isEqualTo(2L);
    }

    @Test
    @DisplayName("공유하면 공유 횟수를 증가시킬 수 있다")
    void 공유하면_공유_횟수를_증가시킬_수_있다() {
        // given
        MeetingEntity meetingEntity = getMeetingEntityData(1L).limitParticipant(10).build();
        MeetingEntity savedMeeting = meetingRepository.save(meetingEntity);
        entityManager.clear();
        // when
        meetingCommandServiceV1.incrementShareCount(savedMeeting.getId());
        // then
        MeetingEntity meeting = meetingRepository.findById(savedMeeting.getId()).get();
        assertThat(meeting.getShareCount()).isEqualTo(1L);
    }

    @Test
    @DisplayName("모임의 인원이 정원인 경우 모임 완료 처리 된다")
    void 모임의_인원이_정원인_경우_모임_완료_처리_된다() {
        // given
        List<Long> meetingIds = new ArrayList<>();
        int limit = 5;
        long leader = 1L;
        for (int i = 0; i < 10; i++) {
            userRepository.save(userInfoEntityData(i + "email@naver.com", "내이름" + i).build());
            MeetingEntity meeting = getMeetingEntityData(leader).limitParticipant(limit).build();
            meetingIds.add(meetingRepository.save(meeting).getId());
            meetingParticipantRepository.save(
                    getLeaderMeetingParticipantEntity(meeting.getId(), leader));
        }

        // when: 모임정원
        for (int i = 1; i < limit; i++) {
            for (int j = i; j < limit; j++) {
                meetingParticipantRepository.save(
                        getParticipantMeetingParticipantEntity((long) i, (long) j + 1));
            }
            meetingCommandServiceV1.updateMeetingState((long) i, MeetingState.COMPLETE);
        }

        // then
        List<MeetingEntity> meetingEntities = meetingRepository.findByIdIn(meetingIds);
        int index = 0;
        while (limit == index) {
            MeetingEntity meetingEntity = meetingEntities.get(index);
            assertThat(meetingEntity.getState().toString()).isEqualTo(COMPLETE.toString());
        }
    }
}
