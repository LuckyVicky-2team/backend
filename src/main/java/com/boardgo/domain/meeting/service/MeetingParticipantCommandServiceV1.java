package com.boardgo.domain.meeting.service;

import com.boardgo.common.exception.CustomIllegalArgumentException;
import com.boardgo.common.exception.CustomNullPointException;
import com.boardgo.domain.mapper.MeetingParticipantMapper;
import com.boardgo.domain.mapper.MeetingParticipateWaitingMapper;
import com.boardgo.domain.meeting.controller.request.MeetingParticipateRequest;
import com.boardgo.domain.meeting.entity.AcceptState;
import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.entity.MeetingParticipantEntity;
import com.boardgo.domain.meeting.entity.MeetingParticipantSubEntity;
import com.boardgo.domain.meeting.entity.MeetingParticipateWaitingEntity;
import com.boardgo.domain.meeting.entity.ParticipantType;
import com.boardgo.domain.meeting.repository.MeetingParticipantRepository;
import com.boardgo.domain.meeting.repository.MeetingParticipantSubRepository;
import com.boardgo.domain.meeting.repository.MeetingParticipateWaitingRepository;
import com.boardgo.domain.meeting.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MeetingParticipantCommandServiceV1 implements MeetingParticipantCommandUseCase {

    private final MeetingParticipantRepository meetingParticipantRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingParticipantSubRepository meetingParticipantSubRepository;
    private final MeetingParticipateWaitingRepository meetingParticipateWaitingRepository;
    private final MeetingParticipantMapper meetingParticipantMapper;
    private final MeetingParticipateWaitingMapper meetingParticipateWaitingMapper;

    @Override
    public void participateMeeting(MeetingParticipateRequest participateRequest, Long userId) {
        MeetingEntity meetingEntity =
                meetingRepository
                        .findById(participateRequest.meetingId())
                        .orElseThrow(() -> new CustomNullPointException("모임이 존재하지 않습니다"));
        validateParticipateMeeting(meetingEntity, userId);

        MeetingParticipantEntity participant =
                meetingParticipantMapper.toMeetingParticipantEntity(
                        meetingEntity.getId(), userId, ParticipantType.PARTICIPANT);
        MeetingParticipateWaitingEntity participateWaitingEntity =
                meetingParticipateWaitingMapper.toMeetingParticipateWaitingEntity(
                        meetingEntity.getId(), userId, AcceptState.WAIT);

        switch (meetingEntity.getType()) {
            case FREE -> meetingParticipantRepository.save(participant);
            case ACCEPT -> meetingParticipateWaitingRepository.save(participateWaitingEntity);
        }
    }

    private void validateParticipateMeeting(MeetingEntity meetingEntity, Long userId) {
        meetingEntity.isAfterMeeting();
        if (meetingParticipantRepository.existsByUserInfoIdAndMeetingId(
                userId, meetingEntity.getId())) {
            throw new CustomIllegalArgumentException("이미 참여된 모임 입니다");
        }

        MeetingParticipantSubEntity participantCount =
                meetingParticipantSubRepository
                        .findById(meetingEntity.getId())
                        .orElseThrow(() -> new CustomNullPointException("모임이 존재하지 않습니다"));
        participantCount.checkParticipant(meetingEntity.getLimitParticipant());
        meetingEntity.checkCompleteState();
    }
}
