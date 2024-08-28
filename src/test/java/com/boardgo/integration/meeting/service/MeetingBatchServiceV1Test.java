package com.boardgo.integration.meeting.service;

import static com.boardgo.domain.meeting.entity.enums.MeetingState.COMPLETE;
import static com.boardgo.domain.review.entity.enums.ReviewType.FINISH;
import static com.boardgo.integration.data.MeetingData.getMeetingEntityData;
import static com.boardgo.integration.data.UserInfoData.userInfoEntityData;
import static com.boardgo.integration.fixture.MeetingParticipantFixture.getLeaderMeetingParticipantEntity;
import static com.boardgo.integration.fixture.MeetingParticipantFixture.getParticipantMeetingParticipantEntity;
import static com.boardgo.integration.fixture.UserInfoFixture.socialUserInfoEntity;
import static org.assertj.core.api.Assertions.assertThat;

import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.repository.MeetingParticipantRepository;
import com.boardgo.domain.meeting.repository.MeetingRepository;
import com.boardgo.domain.meeting.service.MeetingBatchServiceV1;
import com.boardgo.domain.user.entity.UserInfoEntity;
import com.boardgo.domain.user.entity.enums.ProviderType;
import com.boardgo.domain.user.repository.UserRepository;
import com.boardgo.integration.support.IntegrationTestSupport;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MeetingBatchServiceV1Test extends IntegrationTestSupport {

    @Autowired private MeetingBatchServiceV1 meetingBatchService;
    @Autowired private MeetingRepository meetingRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private MeetingParticipantRepository meetingParticipantRepository;

    @Test
    @DisplayName("현재 보다 모임날짜가 지난 모임은 모임 종료 처리 된다")
    void 현재_보다_모임날짜가_지난_모임은_모임_종료_처리_된다() {
        // given
        UserInfoEntity user = userRepository.save(socialUserInfoEntity(ProviderType.KAKAO));
        List<Long> meetingIds = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MeetingEntity meeting =
                    getMeetingEntityData(user.getId())
                            .meetingDatetime(LocalDateTime.now().minusDays(i))
                            .build();
            meetingIds.add(meetingRepository.save(meeting).getId());
        }

        // when
        meetingBatchService.updateFinishMeetingState();

        // then
        List<MeetingEntity> meetingEntities = meetingRepository.findByIdIn(meetingIds);
        meetingEntities.forEach(
                meetingEntity -> {
                    assertThat(meetingEntity.getState().toString()).isEqualTo(FINISH.toString());
                });
    }

    @Test
    @DisplayName("모임의 인원이 정원인 경우 모임 완료 처리 된다")
    void 모임의_인원이_정원인_경우_모임_완료_처리_된다() {
        // given
        List<Long> meetingIds = new ArrayList<>();
        int limit = 5;
        for (int i = 0; i < 10; i++) {
            userRepository.save(
                    userInfoEntityData()
                            .email(i + "email@naver.com")
                            .nickName("내이름" + i)
                            .providerType(ProviderType.LOCAL)
                            .build());
            MeetingEntity meeting = getMeetingEntityData(1L).limitParticipant(limit).build();
            meetingIds.add(meetingRepository.save(meeting).getId());
            meetingParticipantRepository.save(
                    getLeaderMeetingParticipantEntity(meeting.getId(), 1L));
        }
        // 모임정원
        for (int i = 1; i < limit; i++) {
            for (int j = i; j < limit; j++) {
                meetingParticipantRepository.save(
                        getParticipantMeetingParticipantEntity((long) i, (long) j));
            }
        }

        // when
        meetingBatchService.updateCompleteMeetingState();

        // then
        List<MeetingEntity> meetingEntities = meetingRepository.findByIdIn(meetingIds);
        int index = 0;
        while (limit == index) {
            MeetingEntity meetingEntity = meetingEntities.get(index);
            assertThat(meetingEntity.getState().toString()).isEqualTo(COMPLETE.toString());
        }
    }
}
