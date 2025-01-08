package com.boardgo.domain.meeting.service;

import static com.boardgo.domain.meeting.entity.enums.MeetingState.PROGRESS;
import static com.boardgo.domain.meeting.entity.enums.ParticipantType.OUT;

import com.boardgo.common.exception.CustomIllegalArgumentException;
import com.boardgo.common.exception.CustomNullPointException;
import com.boardgo.domain.mapper.MeetingParticipantMapper;
import com.boardgo.domain.meeting.controller.request.MeetingParticipateRequest;
import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.entity.MeetingParticipantEntity;
import com.boardgo.domain.meeting.entity.MeetingParticipantSubEntity;
import com.boardgo.domain.meeting.entity.enums.ParticipantType;
import com.boardgo.domain.meeting.repository.MeetingParticipantRepository;
import com.boardgo.domain.meeting.repository.MeetingParticipantSubRepository;
import com.boardgo.domain.meeting.repository.MeetingRepository;
import java.util.Objects;
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
    private final MeetingParticipantMapper meetingParticipantMapper;

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
        meetingParticipantRepository.save(participant);
    }

    @Override
    public void outMeeting(Long meetingId, Long userId) {
        MeetingParticipantEntity meetingParticipant =
                meetingParticipantRepository.findByUserInfoIdAndMeetingId(userId, meetingId);
        if (Objects.isNull(meetingParticipant)) {
            throw new CustomIllegalArgumentException("참여하지 않은 모임입니다");
        }
        meetingParticipant.updateParticipantType(OUT);
    }

    @Override
    public void create(MeetingParticipantEntity meetingParticipantEntity) {
        meetingParticipantRepository.save(meetingParticipantEntity);
    }

    @Override
    public void deleteByMeetingId(Long meetingId) {
        meetingParticipantRepository.deleteAllInBatchByMeetingId(meetingId);
    }

    private void validateParticipateMeeting(MeetingEntity meetingEntity, Long userId) {
        meetingEntity.isAfterMeeting();
        if (!meetingEntity.isSameState(PROGRESS)) {
            throw new CustomIllegalArgumentException("모임 중인 상태만 참여할 수 있습니다.");
        }
        if (meetingParticipantRepository.existsByUserInfoIdAndMeetingId(
                userId, meetingEntity.getId())) {
            throw new CustomIllegalArgumentException("이미 참여된 모임 입니다");
        }

        MeetingParticipantSubEntity participantCount =
                meetingParticipantSubRepository
                        .findById(meetingEntity.getId())
                        .orElseThrow(() -> new CustomNullPointException("모임이 존재하지 않습니다"));
        if (!participantCount.isParticipated(meetingEntity.getLimitParticipant())) {
            throw new CustomIllegalArgumentException("모임 정원으로 참가 불가능 합니다");
        }
        meetingEntity.checkCompleteState();
    }
}
