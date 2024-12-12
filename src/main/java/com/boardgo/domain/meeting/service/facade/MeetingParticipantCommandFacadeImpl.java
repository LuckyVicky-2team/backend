package com.boardgo.domain.meeting.service.facade;

import static com.boardgo.domain.meeting.entity.enums.MeetingState.PROGRESS;
import static com.boardgo.domain.notification.entity.MessageType.KICKED_OUT;

import com.boardgo.domain.meeting.service.MeetingCommandUseCase;
import com.boardgo.domain.meeting.service.MeetingParticipantCommandUseCase;
import com.boardgo.domain.notification.service.facade.NotificationCommandFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeetingParticipantCommandFacadeImpl implements MeetingParticipantCommandFacade {
    private final MeetingParticipantCommandUseCase meetingParticipantCommandUseCase;
    private final MeetingCommandUseCase meetingCommandUseCase;
    private final NotificationCommandFacade notificationCommandFacade;

    @Override
    public void outMeeting(Long meetingId, Long userId, boolean isKicked) {
        meetingParticipantCommandUseCase.outMeeting(meetingId, userId);
        meetingCommandUseCase.updateMeetingState(meetingId, PROGRESS);
        if (isKicked) {
            notificationCommandFacade.create(meetingId, userId, KICKED_OUT);
        }
    }
}
