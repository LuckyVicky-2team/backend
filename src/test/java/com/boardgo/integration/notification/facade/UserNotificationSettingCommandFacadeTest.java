package com.boardgo.integration.notification.facade;

import static com.boardgo.integration.data.TermsConditionsData.getTermsConditions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Mockito.mock;

import com.boardgo.config.UserNotificationSettingCommandFacadeTestConfig;
import com.boardgo.domain.notification.controller.request.UserNotificationSettingUpdateRequest;
import com.boardgo.domain.notification.entity.MessageType;
import com.boardgo.domain.notification.entity.NotificationSettingEntity;
import com.boardgo.domain.notification.entity.UserNotificationSettingEntity;
import com.boardgo.domain.notification.service.UserNotificationSettingCommandUseCase;
import com.boardgo.domain.notification.service.UserNotificationSettingQueryUseCase;
import com.boardgo.domain.notification.service.facade.UserNotificationSettingCommandFacade;
import com.boardgo.domain.termsconditions.entity.TermsConditionsEntity;
import com.boardgo.domain.termsconditions.entity.UserTermsConditionsEntity;
import com.boardgo.domain.termsconditions.entity.enums.TermsConditionsType;
import com.boardgo.domain.termsconditions.repository.TermsConditionsRepository;
import com.boardgo.domain.termsconditions.repository.UserTermsConditionsRepository;
import com.boardgo.integration.support.IntegrationTestSupport;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({UserNotificationSettingCommandFacadeTestConfig.class})
public class UserNotificationSettingCommandFacadeTest extends IntegrationTestSupport {

    @Autowired private UserNotificationSettingCommandFacade userNotificationSettingCommandFacade;
    @Autowired private UserNotificationSettingCommandUseCase userNotificationSettingCommandUseCase;
    @Autowired private UserNotificationSettingQueryUseCase userNotificationSettingQueryUseCase;
    @Autowired private UserTermsConditionsRepository userTermsConditionsRepository;
    @Autowired private TermsConditionsRepository termsConditionsRepository;

    @ParameterizedTest
    @EnumSource(MessageType.class)
    @DisplayName("회원의 기존 푸시 약관동의가 N 일때 특정 알림설정을 Y로 변경하면 푸시 약관동의도 Y 로 변경된다")
    void 회원의_기존_푸시_약관동의가_N_일때_특정_알림설정을_Y로_변경하면_푸시_약관동의도_Y_로_변경된다(MessageType messageType) {
        // given
        Long userId = 1L;
        UserNotificationSettingUpdateRequest request =
                new UserNotificationSettingUpdateRequest(messageType, true);
        UserNotificationSettingEntity userNotificationSetting =
                UserNotificationSettingEntity.builder()
                        .userInfoId(userId)
                        .notificationSetting(mock(NotificationSettingEntity.class))
                        .isAgreed(Boolean.TRUE)
                        .build();

        willAnswer(update -> userNotificationSetting)
                .given(userNotificationSettingCommandUseCase)
                .update(userId, request.isAgreed(), request.messageType());

        CompletableFuture<Void> dummyFuture =
                CompletableFuture.runAsync(
                        () -> { // 회원 푸시약관동의 비활성화
                            TermsConditionsEntity termsConditions =
                                    termsConditionsRepository.save(
                                            getTermsConditions(TermsConditionsType.PUSH)
                                                    .required(false)
                                                    .build());
                            userTermsConditionsRepository.save(
                                    UserTermsConditionsEntity.builder()
                                            .userInfoId(1L)
                                            .termsConditionsEntity(termsConditions)
                                            .agreement(Boolean.FALSE)
                                            .build());
                        });
        dummyFuture.join();

        // when
        CompletableFuture<Void> resultFuture =
                dummyFuture.thenRun(
                        () -> userNotificationSettingCommandFacade.update(userId, request));

        // then
        assertThat(resultFuture.isCompletedExceptionally()).isFalse();
        resultFuture.thenRun(
                () -> {
                    assertThat(userNotificationSetting.getIsAgreed()).isTrue();
                    UserTermsConditionsEntity userTermsConditionsEntity =
                            userTermsConditionsRepository.findByUserInfoIdAndTermsConditionsType(
                                    userId, TermsConditionsType.PUSH);
                    assertThat(userTermsConditionsEntity.getAgreement()).isTrue();
                });
    }

    @Test
    @DisplayName("회원의 모든 알림설정이 N 이라면 푸시 약관동의를 N 변경한다")
    void 회원의_모든_알림설정이_N_이라면_푸시_약관동의를_N_변경한다() {
        // given
        Long userId = 1L;
        boolean isAgreed = false;
        MessageType messageType = MessageType.MEETING_REMINDER;
        UserNotificationSettingUpdateRequest request =
                new UserNotificationSettingUpdateRequest(messageType, isAgreed);

        UserNotificationSettingEntity userNotificationSetting =
                UserNotificationSettingEntity.builder()
                        .userInfoId(userId)
                        .notificationSetting(mock(NotificationSettingEntity.class))
                        .isAgreed(isAgreed)
                        .build();

        willAnswer(update -> userNotificationSetting)
                .given(userNotificationSettingCommandUseCase)
                .update(userId, request.isAgreed(), request.messageType());
        given(userNotificationSettingQueryUseCase.getUserNotificationSettingsList(userId))
                .willReturn(anyList());

        CompletableFuture<Void> dummyFuture =
                CompletableFuture.runAsync(
                        () -> { // 회원 푸시약관동의 활성화
                            TermsConditionsEntity termsConditions =
                                    termsConditionsRepository.save(
                                            getTermsConditions(TermsConditionsType.PUSH)
                                                    .required(true)
                                                    .build());
                            userTermsConditionsRepository.save(
                                    UserTermsConditionsEntity.builder()
                                            .userInfoId(userId)
                                            .termsConditionsEntity(termsConditions)
                                            .agreement(Boolean.TRUE)
                                            .build());
                        });

        // when
        CompletableFuture<Void> resultFuture =
                dummyFuture.thenRun(
                        () -> userNotificationSettingCommandFacade.update(userId, request));

        // then
        assertThat(resultFuture.isCompletedExceptionally()).isFalse();
        resultFuture.thenRun(
                () -> {
                    assertThat(userNotificationSetting.getIsAgreed()).isFalse();
                    UserTermsConditionsEntity userTermsConditionsEntity =
                            userTermsConditionsRepository.findByUserInfoIdAndTermsConditionsType(
                                    userId, TermsConditionsType.PUSH);
                    assertThat(userTermsConditionsEntity.getAgreement()).isFalse();
                });
    }
}
