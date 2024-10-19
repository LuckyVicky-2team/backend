package com.boardgo.integration.meeting.facade;

import static com.boardgo.integration.data.MeetingData.getMeetingEntityData;
import static com.boardgo.integration.data.UserInfoData.userInfoEntityData;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.boardgo.common.exception.CustomIllegalArgumentException;
import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.service.facade.MeetingParticipantCommandFacade;
import com.boardgo.domain.notification.repository.NotificationRepository;
import com.boardgo.domain.user.entity.UserInfoEntity;
import com.boardgo.integration.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MeetingParticipantCommandFacadeTest extends IntegrationTestSupport {
    @Autowired private MeetingParticipantCommandFacade meetingParticipantCommandFacade;
    @Autowired private NotificationRepository notificationRepository;

    @Test
    @DisplayName("모임 내보내기 시 모임을 나가는 도중 예외가 발생할 경우 알림메세지를 저장하지 않는다")
    void 모임_내보내기_시_모임을_나가는_도중_예외가_발생할_경우_알림메세지를_저장하지_않는다() {
        // given
        UserInfoEntity notParticipatedUser = userInfoEntityData("out@daum.net", "나는강퇴자").build();
        Long leaderId = 2L;
        MeetingEntity meeting = getMeetingEntityData(leaderId).build();

        // when
        assertThrows(
                CustomIllegalArgumentException.class,
                () ->
                        meetingParticipantCommandFacade.outMeeting(
                                meeting.getId(), notParticipatedUser.getId(), true));

        // then
        assertThat(notificationRepository.findAll()).isEmpty();
    }
}
