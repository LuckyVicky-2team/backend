package com.boardgo.domain.mapper;

import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.repository.projection.MeetingReviewProjection;
import com.boardgo.domain.review.service.response.ReviewMeetingResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReviewMapper {
    BoardGameMapper INSTANCE = Mappers.getMapper(BoardGameMapper.class);

    List<ReviewMeetingResponse> toReviewMeetingReviewMeetingFromEntity(
            List<MeetingEntity> meetingEntities);

    List<ReviewMeetingResponse> toReviewMeetingReviewMeetingFromProjection(
            List<MeetingReviewProjection> meetingReviewProjection);
}
