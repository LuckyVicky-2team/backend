package com.boardgo.domain.notification.service.facade;

import com.boardgo.domain.notification.controller.request.UserNotificationSettingUpdateRequest;
import com.boardgo.domain.notification.entity.UserNotificationSettingEntity;
import com.boardgo.domain.notification.service.UserNotificationSettingQueryUseCase;
import com.boardgo.domain.notification.service.response.UserNotificationSettingResponse;
import com.boardgo.domain.termsconditions.entity.UserTermsConditionsEntity;
import com.boardgo.domain.termsconditions.entity.enums.TermsConditionsType;
import com.boardgo.domain.termsconditions.service.UserTermsConditionsQueryUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserNotificationSettingCommandFacadeImpl
        implements UserNotificationSettingCommandFacade {

    private final UserNotificationSettingQueryUseCase userNotificationSettingQueryUseCase;
    private final UserTermsConditionsQueryUseCase userTermsConditionsQueryUseCase;

    @Override
    public void update(Long userId, UserNotificationSettingUpdateRequest request) {
        UserNotificationSettingEntity userNotificationSettingEntity =
                userNotificationSettingQueryUseCase.getUserNotificationSetting(
                        userId, request.messageType());
        userNotificationSettingEntity.updateAgree(request.isAgreed());
        // TODO. 비동기처리
        UserTermsConditionsEntity userTermsConditionsEntity =
                userTermsConditionsQueryUseCase.getUserTermsConditionsEntity(
                        userId, TermsConditionsType.PUSH);

        // 알림설정이 Y 이고, 회원의 기존 푸시 약관동의가 N 이라면 Y로 변경
        if (request.isAgreed() && !userTermsConditionsEntity.getAgreement()) {
            userTermsConditionsEntity.updateAgreement(Boolean.TRUE);
            return;
        }
        // 회원의 모든 알림설정이 N 이라면 푸시약관동의 N 변경
        List<UserNotificationSettingResponse> userNotificationSettings =
                userNotificationSettingQueryUseCase.getUserNotificationSettingsList(userId);
        // TODO. 위에서 변경감지 반영된 데이터가 조회되는건지 확인
        boolean flag = true;
        for (UserNotificationSettingResponse setting : userNotificationSettings) {
            if (setting.isAgreed()) {
                flag = false;
                break;
            }
        }
        if (flag) {
            userTermsConditionsEntity.updateAgreement(Boolean.FALSE);
        }
    }
}
