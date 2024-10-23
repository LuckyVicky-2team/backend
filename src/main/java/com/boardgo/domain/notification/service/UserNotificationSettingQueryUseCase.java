package com.boardgo.domain.notification.service;

import com.boardgo.domain.notification.service.response.UserNotificationSettingResponse;
import java.util.List;

public interface UserNotificationSettingQueryUseCase {
    List<UserNotificationSettingResponse> getUserNotificationSettingsList(Long userId);
}
