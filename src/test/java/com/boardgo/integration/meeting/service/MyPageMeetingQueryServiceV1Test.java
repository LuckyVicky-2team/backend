package com.boardgo.integration.meeting.service;

import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.entity.MeetingParticipantEntity;
import com.boardgo.domain.meeting.entity.enums.MeetingType;
import com.boardgo.domain.meeting.entity.enums.MyPageMeetingFilter;
import com.boardgo.domain.meeting.repository.MeetingParticipantRepository;
import com.boardgo.domain.meeting.repository.MeetingRepository;
import com.boardgo.domain.meeting.service.MyPageMeetingQueryUseCase;
import com.boardgo.domain.meeting.service.response.MeetingMyPageResponse;
import com.boardgo.domain.user.entity.UserInfoEntity;
import com.boardgo.domain.user.entity.enums.ProviderType;
import com.boardgo.domain.user.repository.UserRepository;
import com.boardgo.domain.user.service.dto.CustomUserDetails;
import com.boardgo.integration.fixture.MeetingFixture;
import com.boardgo.integration.fixture.MeetingParticipantFixture;
import com.boardgo.integration.fixture.UserInfoFixture;
import com.boardgo.integration.support.IntegrationTestSupport;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class MyPageMeetingQueryServiceV1Test extends IntegrationTestSupport {
    @Autowired private UserRepository userRepository;
    @Autowired private MeetingRepository meetingRepository;
    @Autowired private MeetingParticipantRepository meetingParticipantRepository;
    @Autowired private MyPageMeetingQueryUseCase myPageMeetingQueryUseCase;

    @Test
    @DisplayName("내가 만든 모임을 가져올 수 있다")
    void 내가_만든_모임을_가져올_수_있다() {
        // given
        UserInfoEntity userInfoEntity = UserInfoFixture.localUserInfoEntity();
        UserInfoEntity savedUser = userRepository.save(userInfoEntity);
        UserInfoEntity userInfoEntity2 = UserInfoFixture.socialUserInfoEntity(ProviderType.KAKAO);
        UserInfoEntity savedUser2 = userRepository.save(userInfoEntity2);
        setSecurityContext(savedUser.getId(), savedUser.getPassword());
        MeetingEntity meetingEntity1 =
                MeetingFixture.getProgressMeetingEntity(savedUser.getId(), MeetingType.FREE, 5);
        MeetingEntity meetingEntity2 =
                MeetingFixture.getProgressMeetingEntity(savedUser.getId(), MeetingType.FREE, 10);
        MeetingEntity savedMeeting1 = meetingRepository.save(meetingEntity1);
        MeetingEntity savedMeeting2 = meetingRepository.save(meetingEntity2);
        MeetingParticipantEntity leaderMeetingParticipantEntity1 =
                MeetingParticipantFixture.getLeaderMeetingParticipantEntity(
                        savedMeeting1.getId(), savedUser.getId());
        MeetingParticipantEntity leaderMeetingParticipantEntity2 =
                MeetingParticipantFixture.getLeaderMeetingParticipantEntity(
                        savedMeeting2.getId(), savedUser.getId());
        MeetingParticipantEntity participantMeetingParticipantEntity =
                MeetingParticipantFixture.getParticipantMeetingParticipantEntity(
                        savedMeeting1.getId(), savedUser2.getId());
        meetingParticipantRepository.save(leaderMeetingParticipantEntity1);
        meetingParticipantRepository.save(leaderMeetingParticipantEntity2);
        meetingParticipantRepository.save(participantMeetingParticipantEntity);
        // when
        List<MeetingMyPageResponse> result =
                myPageMeetingQueryUseCase.findByFilter(MyPageMeetingFilter.CREATE);
        // then
        Assertions.assertThat(result)
                .extracting(MeetingMyPageResponse::id)
                .containsExactlyInAnyOrder(savedMeeting1.getId(), savedMeeting2.getId());
        Assertions.assertThat(result)
                .extracting(MeetingMyPageResponse::currentParticipant)
                .containsExactlyInAnyOrder(1, 2);
    }

    private void setSecurityContext(Long userId, String password) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserInfoEntity userInfoEntity =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));
        CustomUserDetails customUserDetails = new CustomUserDetails(userInfoEntity);

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        customUserDetails, password, customUserDetails.getAuthorities());
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
    }
}
