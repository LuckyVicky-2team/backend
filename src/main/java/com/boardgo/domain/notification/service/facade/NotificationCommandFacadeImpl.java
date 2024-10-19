package com.boardgo.domain.notification.service.facade;

import com.boardgo.domain.meeting.service.MeetingQueryUseCase;
import com.boardgo.domain.notification.entity.MessageType;
import com.boardgo.domain.notification.service.NotificationCommandUseCase;
import com.boardgo.domain.notification.service.request.NotificationCreateRequest;
import com.boardgo.domain.user.service.UserQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationCommandFacadeImpl implements NotificationCommandFacade {

    private final NotificationCommandUseCase notificationCommandUseCase;
    private final UserQueryUseCase userQueryUseCase;
    private final MeetingQueryUseCase meetingQueryUseCase;

    @Override
    public void create(Long meetingId, Long userId, MessageType messageType) {
        String nickname = userQueryUseCase.getUserInfoEntity(userId).getNickName();
        String meetingTitle = meetingQueryUseCase.getMeeting(meetingId).getTitle();
        notificationCommandUseCase.createNotification(
                messageType,
                new NotificationCreateRequest(meetingTitle, nickname, meetingId, userId));
    }
}
