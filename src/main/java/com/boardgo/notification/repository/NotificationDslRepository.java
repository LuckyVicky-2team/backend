package com.boardgo.notification.repository;

import com.boardgo.notification.repository.projection.NotificationProjection;
import java.util.List;

public interface NotificationDslRepository {

    List<NotificationProjection> findByUserInfoId(Long userId);
}
