package com.boardgo.domain.mapper;

import com.boardgo.notification.repository.projection.NotificationProjection;
import com.boardgo.notification.service.response.NotificationResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NotificationMapper {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    List<NotificationResponse> toResponseList(List<NotificationProjection> notificationProjections);
}
