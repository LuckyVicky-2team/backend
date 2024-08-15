package com.boardgo.domain.meeting.service;

import static com.boardgo.domain.meeting.entity.MeetingState.COMPLETE;

import com.boardgo.common.exception.CustomIllegalArgumentException;
import com.boardgo.common.exception.CustomNullPointException;
import com.boardgo.domain.mapper.MeetingParticipantMapper;
import com.boardgo.domain.meeting.controller.request.MeetingParticipateRequest;
import com.boardgo.domain.meeting.entity.AcceptState;
import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.entity.MeetingParticipantEntity;
import com.boardgo.domain.meeting.entity.MeetingParticipantSubEntity;
import com.boardgo.domain.meeting.entity.MeetingParticipateWaitingEntity;
import com.boardgo.domain.meeting.entity.MeetingState;
import com.boardgo.domain.meeting.entity.ParticipantType;
import com.boardgo.domain.meeting.repository.MeetingParticipantRepository;
import com.boardgo.domain.meeting.repository.MeetingParticipantSubRepository;
import com.boardgo.domain.meeting.repository.MeetingParticipateWaitingRepository;
import com.boardgo.domain.meeting.repository.MeetingRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingParticipantCommandServiceV1 implements MeetingParticipantCommandUseCase {

    private final MeetingParticipantRepository meetingParticipantRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingParticipantSubRepository meetingParticipantSubRepository;
    private final MeetingParticipateWaitingRepository meetingParticipateWaitingRepository;
    private final MeetingParticipantMapper meetingParticipantMapper;

    @Override
    public void participateMeeting(MeetingParticipateRequest participateRequest, Long userId) {
        // TODO. 에러 문서화
        MeetingEntity meetingEntity =
                meetingRepository
                        .findById(participateRequest.meetingId())
                        .orElseThrow(() -> new CustomNullPointException("모임이 존재하지 않습니다"));
        if (isAfterMeeting(meetingEntity.getMeetingDatetime())) {
            throw new CustomIllegalArgumentException("모임 날짜가 지난 모임으로 참가 불가능 합니다");
        }
        if (meetingParticipateWaitingRepository.existsByUserInfoId(userId)) {
            throw new CustomIllegalArgumentException("이미 참여한 모임 입니다");
        }

        MeetingParticipantSubEntity participantCount =
                meetingParticipantSubRepository
                        .findById(meetingEntity.getId())
                        .orElseThrow(() -> new CustomNullPointException("모임이 존재하지 않습니다"));
        isParticipation(meetingEntity, participantCount);

        // FIXME: 저장 기능 리팩토링 필요
        MeetingParticipantEntity participant =
                getMeetingParticipantEntity(meetingEntity.getId(), userId);
        MeetingParticipateWaitingEntity participateWaitingEntity =
                getMeetingParticipateWaitingEntity(meetingEntity.getId(), userId);

        switch (meetingEntity.getType()) {
            case FREE -> meetingParticipantRepository.save(participant);
            case ACCEPT -> meetingParticipateWaitingRepository.save(participateWaitingEntity);
        }
    }

    /**
     * @return 현재 시간이 모임날짜 보다 미래일 경우 true, 아닐 경우 false
     */
    private boolean isAfterMeeting(LocalDateTime meetingDateTime) {
        return LocalDateTime.now().isAfter(meetingDateTime);
    }

    private boolean isParticipation(
            MeetingEntity meetingEntity, MeetingParticipantSubEntity participantCount) {
        Integer currentCount = participantCount.getParticipantCount();
        Integer limitCount = meetingEntity.getLimitParticipant();
        MeetingState currentState = meetingEntity.getState();

        if (currentCount >= limitCount) {
            throw new CustomIllegalArgumentException("모임 정원으로 참가 불가능 합니다");
        }
        if (COMPLETE == currentState) {
            throw new CustomIllegalArgumentException("모집 완료된 모임으로 참가 불가능 합니다");
        }
        return true;
    }

    // FIXME MapStruct로 리팩토리 필요
    private MeetingParticipantEntity getMeetingParticipantEntity(Long meetingId, Long userId) {
        return MeetingParticipantEntity.builder()
                .meetingId(meetingId)
                .userInfoId(userId)
                .type(ParticipantType.PARTICIPANT)
                .build();
    }

    // FIXME MapStruct로 리팩토리 필요
    private MeetingParticipateWaitingEntity getMeetingParticipateWaitingEntity(
            Long meetingId, Long userId) {
        return MeetingParticipateWaitingEntity.builder()
                .meetingId(meetingId)
                .userInfoId(userId)
                .acceptState(AcceptState.WAIT)
                .build();
    }
}
