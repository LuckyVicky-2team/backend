package com.boardgo.integration.notification.service;

import static com.boardgo.integration.data.UserInfoData.userInfoEntityData;
import static com.boardgo.integration.fixture.NotificationSettingFixture.getNotificationSettings;
import static org.assertj.core.api.Assertions.assertThat;

import com.boardgo.domain.notification.entity.MessageType;
import com.boardgo.domain.notification.entity.NotificationSettingEntity;
import com.boardgo.domain.notification.entity.UserNotificationSettingEntity;
import com.boardgo.domain.notification.repository.NotificationSettingRepository;
import com.boardgo.domain.notification.repository.UserNotificationSettingRepository;
import com.boardgo.domain.notification.service.UserNotificationSettingQueryUseCase;
import com.boardgo.domain.notification.service.response.UserNotificationSettingResponse;
import com.boardgo.domain.user.entity.UserInfoEntity;
import com.boardgo.domain.user.repository.UserRepository;
import com.boardgo.integration.support.IntegrationTestSupport;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserNotificationSettingQueryServiceV1Test extends IntegrationTestSupport {

    @Autowired UserNotificationSettingQueryUseCase userNotificationSettingQueryUseCase;
    @Autowired UserRepository userRepository;
    @Autowired NotificationSettingRepository notificationSettingRepository;
    @Autowired UserNotificationSettingRepository userNotificationSettingRepository;

    @Test
    @DisplayName("회원의 알림설정 조회하기")
    void 회원의_알림설정_조회하기() {
        // given
        UserInfoEntity user = userInfoEntityData("user1@naver.com", "user1").build();
        userRepository.save(user);

        List<NotificationSettingEntity> notificationSettings = getNotificationSettings();
        notificationSettingRepository.saveAll(notificationSettings);
        notificationSettings.forEach(
                notificationSettingEntity -> {
                    userNotificationSettingRepository.save(
                            UserNotificationSettingEntity.builder()
                                    .userInfoId(user.getId())
                                    .notificationSetting(notificationSettingEntity)
                                    .isAgreed(true)
                                    .build());
                });

        // when
        List<UserNotificationSettingResponse> userNotificationSettingsList =
                userNotificationSettingQueryUseCase.getUserNotificationSettingsList(user.getId());

        // then
        assertThat(userNotificationSettingsList).isNotEmpty();
        List<MessageType> messageTypes = Arrays.stream(MessageType.values()).toList();

        userNotificationSettingsList.forEach(
                response -> {
                    assertThat(response.isAgreed()).isNotNull().isTrue();
                    assertThat(response.content()).isNotNull();
                    assertThat(response.additionalContent()).isNotNull();
                    assertThat(messageTypes.contains(response.messageType())).isTrue();
                });
    }
}
