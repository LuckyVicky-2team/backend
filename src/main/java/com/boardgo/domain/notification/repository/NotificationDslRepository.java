package com.boardgo.domain.notification.repository;

import com.boardgo.domain.notification.repository.projection.NotificationProjection;
import java.util.List;

public interface NotificationDslRepository {

    List<NotificationProjection> findByUserInfoId(Long userId);
}