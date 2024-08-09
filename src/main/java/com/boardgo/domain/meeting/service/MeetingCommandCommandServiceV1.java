package com.boardgo.domain.meeting.service;

import com.boardgo.common.exception.CustomNoSuchElementException;
import com.boardgo.common.exception.advice.dto.ErrorCode;
import com.boardgo.common.utils.FileUtils;
import com.boardgo.common.utils.S3Service;
import com.boardgo.domain.boardgame.entity.BoardGameEntity;
import com.boardgo.domain.boardgame.repository.BoardGameRepository;
import com.boardgo.domain.mapper.MeetingMapper;
import com.boardgo.domain.meeting.controller.dto.MeetingCreateRequest;
import com.boardgo.domain.meeting.entity.MeetingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingCommandCommandServiceV1 implements MeetingCommandUseCase {
    private final BoardGameRepository boardGameRepository;
    private final MeetingCreateFactory meetingCreateFactory;
    private final MeetingMapper meetingMapper;
    private final S3Service s3Service;

    @Override
    public Long create(MeetingCreateRequest meetingCreateRequest, MultipartFile imageFile) {
        String imageUri = registerImage(meetingCreateRequest, imageFile);
        MeetingEntity meetingEntity = meetingMapper.toMeetingEntity(meetingCreateRequest, imageUri);
        return meetingCreateFactory.create(
                meetingEntity,
                meetingCreateRequest.boardGameIdList(),
                meetingCreateRequest.genreIdList());
    }

    private String registerImage(
            MeetingCreateRequest meetingCreateRequest, MultipartFile imageFile) {
        String imageUri;
        if (imageFile.isEmpty()) {
            BoardGameEntity boardGameEntity =
                    boardGameRepository
                            .findById(meetingCreateRequest.boardGameIdList().getFirst())
                            .orElseThrow(
                                    () ->
                                            new CustomNoSuchElementException(
                                                    ErrorCode.ELEMENT_NOT_FOUND.getCode(),
                                                    "존재하지 않는 보드게임입니다."));
            imageUri = boardGameEntity.getThumbnail();
        } else {
            imageUri = s3Service.upload(FileUtils.getUniqueFileName(imageFile), imageFile);
        }
        return imageUri;
    }
}
