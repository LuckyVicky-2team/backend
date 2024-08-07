package com.boardgo.domain.mapper;

import com.boardgo.domain.meeting.controller.dto.MeetingCreateRequest;
import com.boardgo.domain.meeting.entity.MeetingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MeetingMapper {
    MeetingMapper INSTANCE = Mappers.getMapper(MeetingMapper.class);

    @Mapping(source = "imageUri", target = "thumbnail")
    MeetingEntity toMeetingEntity(MeetingCreateRequest meetingCreateRequest, String imageUri);
}
