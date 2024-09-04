package com.boardgo.integration.meeting.service;

import static com.boardgo.integration.data.MeetingData.getMeetingEntityData;
import static com.boardgo.integration.fixture.MeetingParticipantFixture.getOutMeetingParticipantEntity;
import static com.boardgo.integration.fixture.UserInfoFixture.localUserInfoEntity;

import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.entity.MeetingParticipantEntity;
import com.boardgo.domain.meeting.repository.MeetingParticipantRepository;
import com.boardgo.domain.meeting.repository.MeetingRepository;
import com.boardgo.domain.meeting.service.MeetingParticipantQueryUseCase;
import com.boardgo.domain.meeting.service.response.ParticipantOutResponse;
import com.boardgo.domain.user.entity.UserInfoEntity;
import com.boardgo.domain.user.repository.UserRepository;
import com.boardgo.domain.user.service.response.CustomUserDetails;
import com.boardgo.integration.support.IntegrationTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class MeetingParticipantQueryServiceV1Test extends IntegrationTestSupport {
    @Autowired private UserRepository userRepository;
    @Autowired private MeetingRepository meetingRepository;
    @Autowired private MeetingParticipantRepository meetingParticipantRepository;

    @Autowired private MeetingParticipantQueryUseCase meetingParticipantQueryUseCase;

    @Test
    @DisplayName("방장에 의해 나가진 사용자를 구별할 수 있다")
    void 방장에_의해_나가진_사용자를_구별할_수_있다() {
        // given
        UserInfoEntity userInfoEntity = localUserInfoEntity();
        UserInfoEntity savedUser = userRepository.save(userInfoEntity);
        setSecurityContext(savedUser.getId(), savedUser.getPassword());
        MeetingEntity meetingEntity =
                getMeetingEntityData(savedUser.getId()).limitParticipant(10).build();
        MeetingEntity savedMeeting = meetingRepository.save(meetingEntity);
        MeetingParticipantEntity participantMeetingParticipantEntity =
                getOutMeetingParticipantEntity(savedMeeting.getId(), savedUser.getId());
        meetingParticipantRepository.save(participantMeetingParticipantEntity);
        // when
        ParticipantOutResponse outState =
                meetingParticipantQueryUseCase.getOutState(savedMeeting.getId());
        // then
        Assertions.assertThat(outState.outState()).isEqualTo("OUT");
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
