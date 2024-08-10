package com.boardgo.init;

import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.entity.MeetingParticipantEntity;
import com.boardgo.domain.meeting.entity.MeetingState;
import com.boardgo.domain.meeting.entity.MeetingType;
import com.boardgo.domain.meeting.entity.ParticipantType;
import com.boardgo.domain.meeting.repository.MeetingParticipantRepository;
import com.boardgo.domain.meeting.repository.MeetingRepository;
import java.time.LocalDateTime;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(3)
@Component
@RequiredArgsConstructor
public class MeetingInitializer implements ApplicationRunner {

    private final MeetingRepository meetingRepository;
    private final MeetingParticipantRepository meetingParticipantRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Random random = new Random();

        for (int i = 0; i < 300; i++) {
            MeetingState state;
            int limitNumber = Math.max(i % 10, 2);
            if (i % 2 == 0) {
                state = MeetingState.PROGRESS;
            } else if (i % 3 == 1) {
                state = MeetingState.COMPLETE;
            } else {
                state = MeetingState.FINISH;
            }

            long userId = (long) (i % 30) + 1;
            MeetingEntity savedMeeting =
                    meetingRepository.save(
                            MeetingEntity.builder()
                                    .title("title" + i)
                                    .userId(userId)
                                    .content("content" + i)
                                    .thumbnail("thumbnail" + i)
                                    .type(MeetingType.FREE)
                                    .city("city" + limitNumber)
                                    .county("county" + limitNumber)
                                    .state(state)
                                    .limitParticipant(limitNumber)
                                    .longitude(i + ".12321321")
                                    .latitude(i + ".787878")
                                    .meetingDatetime(LocalDateTime.now().plusDays(i))
                                    .hit((long) i)
                                    .build());

            int participantLimit = Math.max((i - limitNumber) % (limitNumber + 1), 1);

            for (int j = 0; j < participantLimit; j++) {
                MeetingParticipantEntity.MeetingParticipantEntityBuilder builder =
                        MeetingParticipantEntity.builder();
                if (j == 0) {
                    builder.type(ParticipantType.LEADER);
                } else {
                    builder.type(ParticipantType.PARTICIPANT);
                }
                meetingParticipantRepository.save(
                        builder.meetingId(savedMeeting.getId())
                                .userInfoId((userId + j) % 30 + 1)
                                .build());
            }
        }
    }
}
