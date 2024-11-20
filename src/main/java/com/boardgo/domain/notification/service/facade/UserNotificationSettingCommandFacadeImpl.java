package com.boardgo.domain.notification.service.facade;

import com.boardgo.common.exception.CustomIllegalArgumentException;
import com.boardgo.domain.notification.controller.request.UserNotificationSettingUpdateRequest;
import com.boardgo.domain.notification.service.UserNotificationSettingCommandUseCase;
import com.boardgo.domain.notification.service.UserNotificationSettingQueryUseCase;
import com.boardgo.domain.notification.service.response.UserNotificationSettingResponse;
import com.boardgo.domain.termsconditions.service.UserTermsConditionsCommandUseCase;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserNotificationSettingCommandFacadeImpl
        implements UserNotificationSettingCommandFacade {

    private final UserNotificationSettingQueryUseCase userNotificationSettingQueryUseCase;
    private final UserTermsConditionsCommandUseCase userTermsConditionsCommandUseCase;
    private final UserNotificationSettingCommandUseCase userNotificationSettingCommandUseCase;

    @Override
    public void update(Long userId, UserNotificationSettingUpdateRequest request) {
        CompletableFuture<Void> notificationSettingFuture =
                CompletableFuture.runAsync(
                                () ->
                                        userNotificationSettingCommandUseCase.update(
                                                userId, request.isAgreed(), request.messageType()))
                        .exceptionally(
                                throwable -> {
                                    log.error(
                                            "PATCH /user-notification ERROR :: {} REQUEST :: userId {} {}",
                                            throwable.getMessage(),
                                            userId,
                                            request);
                                    throw new CustomIllegalArgumentException(
                                            "알림설정 수정 중 예외가 발생했습니다.");
                                });
        notificationSettingFuture.join();

        CompletableFuture<Void> pushTermsConditionFuture =
                notificationSettingFuture.thenRun(
                        () -> {
                            this.updateUserSettings(userId, request.isAgreed());
                        });
        pushTermsConditionFuture.exceptionally(
                (throwable) -> {
                    log.error(
                            "updateUserSettings ERROR :: {} REQUEST :: userId {} {}",
                            throwable.getMessage(),
                            userId,
                            request);
                    return null;
                });
    }

    // 푸시 설정이 변경 후 회원의 푸시 약관동의가 변경
    private void updateUserSettings(Long userId, Boolean isAgreed) {
        // 알림설정이 Y 이고, 회원의 기존 푸시 약관동의가 N 이라면 Y로 변경
        if (isAgreed) {
            userTermsConditionsCommandUseCase.updatePushTermsCondition(userId);
        } else {
            // 회원의 모든 알림설정이 N 이라면 푸시약관동의 N 변경
            List<UserNotificationSettingResponse> userNotificationSettings =
                    userNotificationSettingQueryUseCase.getUserNotificationSettingsList(userId);
            for (UserNotificationSettingResponse setting : userNotificationSettings) {
                if (setting.isAgreed()) { // 하나라도 true 가 존재하면 패스
                    return;
                }
            }
            userTermsConditionsCommandUseCase.updatePushTermsCondition(userId);
        }
    }
}
