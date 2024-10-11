package com.boardgo.domain.notification.entity;

import static com.boardgo.domain.notification.service.NotificationMessageFactory.replaceMessage;

import com.boardgo.domain.notification.service.NotificationMessageFactory;
import com.boardgo.domain.notification.service.request.ReplaceMessageParam;

public enum MessageType {
    MEETING_MODIFY,
    MEETING_REMINDER,
    REVIEW_RECEIVED,
    REQUEST_REVIEW,
    KICKED_OUT;

    public String createMessage(ReplaceMessageParam param) {
        NotificationMessageFormat messageFormat = NotificationMessageFactory.get(this);
        return switch (this) {
            case REQUEST_REVIEW -> replaceMessage(messageFormat.getTitle(), param);
            default -> replaceMessage(messageFormat.getContent(), param);
        };
    }
}
