package com.boardgo.domain.mapper;

import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.review.service.response.ReviewMeetingsResponse;
import java.util.List;
import org.mapstruct.factory.Mappers;

public interface ReviewMapper {
    BoardGameMapper INSTANCE = Mappers.getMapper(BoardGameMapper.class);

    List<ReviewMeetingsResponse> toReviewMeetingsResponseList(List<MeetingEntity> meetingEntities);
}
