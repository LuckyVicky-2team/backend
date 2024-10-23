package com.boardgo.domain.notification.service;

import com.boardgo.common.exception.CustomIllegalArgumentException;
import com.boardgo.domain.mapper.UserNotificationSettingMapper;
import com.boardgo.domain.notification.entity.UserNotificationSettingEntity;
import com.boardgo.domain.notification.repository.UserNotificationSettingRepository;
import com.boardgo.domain.notification.service.response.UserNotificationSettingResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserNotificationSettingQueryServiceV1 implements UserNotificationSettingQueryUseCase {

    private final UserNotificationSettingRepository userNotificationSettingRepository;
    private final UserNotificationSettingMapper userNotificationSettingMapper;

    @Override
    public List<UserNotificationSettingResponse> getUserNotificationSettingsList(Long userId) {
        List<UserNotificationSettingEntity> entities =
                userNotificationSettingRepository.findByUserInfoId(userId);
        if (entities.isEmpty()) {
            throw new CustomIllegalArgumentException("회원의 알림설정이 존재하지 않습니다");
        }
        return userNotificationSettingMapper.toUserNotificationSettingResponse(entities);
    }
}
