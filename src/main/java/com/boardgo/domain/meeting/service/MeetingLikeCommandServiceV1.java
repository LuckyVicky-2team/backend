package com.boardgo.domain.meeting.service;

import com.boardgo.common.exception.CustomIllegalArgumentException;
import com.boardgo.common.exception.CustomNoSuchElementException;
import com.boardgo.common.utils.SecurityUtils;
import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.repository.MeetingLikeRepository;
import com.boardgo.domain.meeting.repository.MeetingRepository;
import com.boardgo.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingLikeCommandServiceV1 implements MeetingLikeCommandUseCase {

    private final MeetingLikeRepository meetingLikeRepository;
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;

    @Override
    public void createMany(List<Long> meetingIdList) {
        checkValidation(meetingIdList);
        meetingLikeRepository.bulkInsert(meetingIdList, SecurityUtils.currentUserId());
    }

    private void checkValidation(List<Long> meetingIdList) {
        userRepository
                .findById(SecurityUtils.currentUserId())
                .orElseThrow(() -> new CustomNoSuchElementException("유저"));

        List<MeetingEntity> meetingEntities = meetingRepository.findByIdIn(meetingIdList);
        if (meetingEntities.size() != meetingIdList.size()) {
            throw new CustomIllegalArgumentException("모임 ID 중 존재하지 않는 모임이 있습니다.");
        }
    }
}
