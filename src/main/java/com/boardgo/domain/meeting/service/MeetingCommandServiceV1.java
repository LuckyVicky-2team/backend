package com.boardgo.domain.meeting.service;

import static com.boardgo.common.constant.S3BucketConstant.MEETING;
import static com.boardgo.domain.meeting.entity.enums.MeetingState.COMPLETE;

import com.boardgo.common.exception.CustomNoSuchElementException;
import com.boardgo.common.utils.FileUtils;
import com.boardgo.common.utils.S3Service;
import com.boardgo.common.utils.SecurityUtils;
import com.boardgo.domain.boardgame.entity.BoardGameEntity;
import com.boardgo.domain.boardgame.repository.BoardGameRepository;
import com.boardgo.domain.mapper.MeetingMapper;
import com.boardgo.domain.meeting.controller.request.MeetingCreateRequest;
import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.entity.MeetingParticipantSubEntity;
import com.boardgo.domain.meeting.repository.MeetingParticipantRepository;
import com.boardgo.domain.meeting.repository.MeetingParticipantSubRepository;
import com.boardgo.domain.meeting.repository.MeetingRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MeetingCommandServiceV1 implements MeetingCommandUseCase {
    private final BoardGameRepository boardGameRepository;
    private final MeetingCreateFactory meetingCreateFactory;
    private final MeetingRepository meetingRepository;
    private final MeetingParticipantRepository meetingParticipantRepository;
    private final MeetingMapper meetingMapper;
    private final S3Service s3Service;
    private final MeetingParticipantSubRepository meetingParticipantSubRepository;

    @Override
    public Long create(MeetingCreateRequest meetingCreateRequest, MultipartFile imageFile) {
        String imageUri = registerImage(meetingCreateRequest, imageFile);
        Long userId = SecurityUtils.currentUserId();
        MeetingEntity meetingEntity =
                meetingMapper.toMeetingEntity(meetingCreateRequest, userId, imageUri);
        return meetingCreateFactory.create(
                meetingEntity,
                meetingCreateRequest.boardGameIdList(),
                meetingCreateRequest.genreIdList());
    }

    @Override
    public void incrementShareCount(Long meetingId) {
        MeetingEntity meeting = getMeetingEntity(meetingId);
        meeting.incrementShareCount();
    }

    @Override
    public void incrementViewCount(Long meetingId) {
        MeetingEntity meeting = getMeetingEntity(meetingId);
        meeting.incrementViewCount();
    }

    private String registerImage(
            MeetingCreateRequest meetingCreateRequest, MultipartFile imageFile) {
        String imageUri;
        if (Objects.isNull(imageFile) || imageFile.isEmpty()) {
            BoardGameEntity boardGameEntity =
                    boardGameRepository
                            .findById(meetingCreateRequest.boardGameIdList().getFirst())
                            .orElseThrow(() -> new CustomNoSuchElementException("보드게임"));
            imageUri = boardGameEntity.getThumbnail();
        } else {
            imageUri = s3Service.upload(MEETING, FileUtils.getUniqueFileName(imageFile), imageFile);
        }
        return imageUri;
    }

    @Override
    public void updateCompleteMeetingState(Long meetingId) {
        MeetingEntity meeting = getMeetingEntity(meetingId);
        MeetingParticipantSubEntity meetingSubEntity =
                meetingParticipantSubRepository.findById(meetingId).get();
        if (!meetingSubEntity.isParticipated(meeting.getLimitParticipant())) {
            meeting.updateMeetingState(COMPLETE);
        }
    }

    private MeetingEntity getMeetingEntity(Long meetingId) {
        return meetingRepository
                .findById(meetingId)
                .orElseThrow(() -> new CustomNoSuchElementException("모임"));
    }
}
