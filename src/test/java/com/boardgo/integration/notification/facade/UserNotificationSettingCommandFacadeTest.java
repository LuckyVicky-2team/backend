package com.boardgo.integration.notification.facade;

import static com.boardgo.integration.data.TermsConditionsData.getTermsConditions;
import static com.boardgo.integration.fixture.NotificationSettingFixture.getNotificationSettings;
import static org.assertj.core.api.Assertions.assertThat;

import com.boardgo.domain.notification.controller.request.UserNotificationSettingUpdateRequest;
import com.boardgo.domain.notification.entity.MessageType;
import com.boardgo.domain.notification.entity.NotificationSettingEntity;
import com.boardgo.domain.notification.entity.UserNotificationSettingEntity;
import com.boardgo.domain.notification.repository.NotificationSettingRepository;
import com.boardgo.domain.notification.repository.UserNotificationSettingRepository;
import com.boardgo.domain.notification.service.facade.UserNotificationSettingCommandFacade;
import com.boardgo.domain.termsconditions.entity.TermsConditionsEntity;
import com.boardgo.domain.termsconditions.entity.UserTermsConditionsEntity;
import com.boardgo.domain.termsconditions.entity.enums.TermsConditionsType;
import com.boardgo.domain.termsconditions.repository.TermsConditionsRepository;
import com.boardgo.domain.termsconditions.repository.UserTermsConditionsRepository;
import com.boardgo.integration.support.IntegrationTestSupport;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

public class UserNotificationSettingCommandFacadeTest extends IntegrationTestSupport {

    @Autowired private UserNotificationSettingCommandFacade userNotificationSettingCommandFacade;
    @Autowired private UserNotificationSettingRepository userNotificationSettingRepository;
    @Autowired private NotificationSettingRepository notificationSettingRepository;
    @Autowired private UserTermsConditionsRepository userTermsConditionsRepository;
    @Autowired private TermsConditionsRepository termsConditionsRepository;
    private TermsConditionsEntity termsConditionsEntity;

    @BeforeEach
    void init() {
        CompletableFuture.runAsync(
                () -> {
                    termsConditionsEntity =
                            getTermsConditions(TermsConditionsType.PUSH).required(false).build();
                    termsConditionsRepository.save(termsConditionsEntity);
                });
    }

    @ParameterizedTest
    @EnumSource(MessageType.class)
    @DisplayName("회원의 기존 푸시 약관동의가 N 일때 특정 알림설정을 Y로 변경하면 푸시 약관동의도 Y 로 변경된다")
    void 회원의_기존_푸시_약관동의가_N_일때_특정_알림설정을_Y로_변경하면_푸시_약관동의도_Y_로_변경된다(MessageType messageType) {
        // given
        Long userId = 1L;
        CompletableFuture<Void> dummyFuture =
                CompletableFuture.runAsync(
                        () -> {
                            NotificationSettingEntity notificationSetting =
                                    NotificationSettingEntity.builder()
                                            .messageType(messageType)
                                            .content(messageType + " 알림 내용이에요")
                                            .additionalContent(messageType + " 알림에 대한 부가 설명이에요")
                                            .build();
                            notificationSettingRepository.save(notificationSetting);

                            // 회원 알림설정
                            userNotificationSettingRepository.save(
                                    UserNotificationSettingEntity.builder()
                                            .userInfoId(userId)
                                            .notificationSetting(notificationSetting)
                                            .isAgreed(Boolean.FALSE)
                                            .build());
                            // 회원 푸시약관동의 비활성화
                            userTermsConditionsRepository.save(
                                    UserTermsConditionsEntity.builder()
                                            .userInfoId(1L)
                                            .termsConditionsEntity(termsConditionsEntity)
                                            .agreement(Boolean.FALSE)
                                            .build());
                        });
        dummyFuture.join();

        // when
        CompletableFuture<Void> resultFuture =
                dummyFuture.thenRun(
                        () ->
                                userNotificationSettingCommandFacade.update(
                                        userId,
                                        new UserNotificationSettingUpdateRequest(
                                                messageType, true)));

        // then
        assertThat(resultFuture.isCompletedExceptionally()).isFalse();
        resultFuture.thenRun(
                () -> {
                    UserNotificationSettingEntity userNotificationSettingEntity =
                            userNotificationSettingRepository
                                    .findByUserInfoIdAndNotificationSettingMessageType(
                                            userId, messageType);
                    assertThat(userNotificationSettingEntity.getIsAgreed()).isTrue();

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
        List<NotificationSettingEntity> notificationSettingEntities = getNotificationSettings();
        int index = notificationSettingEntities.size() - 1;

        CompletableFuture<Void> dummyFuture =
                CompletableFuture.runAsync(
                        () -> {
                            notificationSettingRepository.saveAll(notificationSettingEntities);

                            // 회원 알림설정
                            boolean isAgreed = Boolean.FALSE;
                            for (int i = 0; i < notificationSettingEntities.size(); i++) {
                                if (i == index) {
                                    isAgreed = Boolean.TRUE;
                                }
                                userNotificationSettingRepository.save(
                                        UserNotificationSettingEntity.builder()
                                                .userInfoId(userId)
                                                .notificationSetting(
                                                        notificationSettingEntities.get(i))
                                                .isAgreed(isAgreed)
                                                .build());
                            }
                            // 회원 푸시약관동의 활성화
                            userTermsConditionsRepository.save(
                                    UserTermsConditionsEntity.builder()
                                            .userInfoId(userId)
                                            .termsConditionsEntity(termsConditionsEntity)
                                            .agreement(Boolean.TRUE)
                                            .build());
                        });

        // when
        NotificationSettingEntity notificationSetting = notificationSettingEntities.get(index);
        CompletableFuture<Void> resultFuture =
                dummyFuture.thenRun(
                        () ->
                                userNotificationSettingCommandFacade.update(
                                        userId,
                                        new UserNotificationSettingUpdateRequest(
                                                notificationSetting.getMessageType(), false)));

        // then
        assertThat(resultFuture.isCompletedExceptionally()).isFalse();
        resultFuture.thenRun(
                () -> {
                    UserNotificationSettingEntity userNotificationSettingEntity =
                            userNotificationSettingRepository
                                    .findByUserInfoIdAndNotificationSettingMessageType(
                                            userId, notificationSetting.getMessageType());
                    assertThat(userNotificationSettingEntity.getIsAgreed()).isFalse();
                    UserTermsConditionsEntity userTermsConditionsEntity =
                            userTermsConditionsRepository.findByUserInfoIdAndTermsConditionsType(
                                    userId, TermsConditionsType.PUSH);
                    assertThat(userTermsConditionsEntity.getAgreement()).isFalse();
                });
    }

    // TODO 비동기 검증: 알림설정 수정 후 푸시 약관동의 수정 작업 완료 유무와 상관없이 알림설정 수정 작업 결과를 반환한다
    // TODO 비동기 예외발생 검증: 알림설정 수정 중 예외가 발생할 경우 푸시 약관동의 수정은 진행하지 않는다.
    // TODO 비동기 예외발생 검증: 알림설정 수정 후 푸시 약관동의에서 예외가 발생해도 알림설정 수정은 적용된다.

}
