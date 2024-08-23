package com.boardgo.domain.meeting.service;

import com.boardgo.common.exception.CustomIllegalArgumentException;
import com.boardgo.common.exception.CustomNoSuchElementException;
import com.boardgo.common.utils.SecurityUtils;
import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.repository.MeetingLikeRepository;
import com.boardgo.domain.meeting.repository.MeetingRepository;
import com.boardgo.domain.user.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        checkLikeValidation(meetingIdList);
        meetingLikeRepository.bulkInsert(meetingIdList, SecurityUtils.currentUserId());
    }

    private void checkLikeValidation(List<Long> meetingIdList) {
        checkUserExist();
        checkMeetingIdListExist(meetingIdList);
    }

    private void checkMeetingIdListExist(List<Long> meetingIdList) {
        List<MeetingEntity> meetingEntities = meetingRepository.findByIdIn(meetingIdList);
        if (meetingEntities.size() != meetingIdList.size()) {
            throw new CustomIllegalArgumentException("모임 ID 중 존재하지 않는 모임이 있습니다.");
        }
        Set<Long> meetingIdSet = new HashSet<>(meetingIdList);
        meetingEntities.stream()
                .map(MeetingEntity::getId)
                .filter(id -> !meetingIdSet.contains(id))
                .findFirst()
                .ifPresent(
                        id -> {
                            throw new CustomIllegalArgumentException(
                                    "모임 ID 중 존재하지 않는 모임이 있습니다: " + id);
                        });
    }

    private void checkUserExist() {
        userRepository
                .findById(SecurityUtils.currentUserId())
                .orElseThrow(() -> new CustomNoSuchElementException("유저"));
    }
}
