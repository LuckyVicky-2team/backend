package com.boardgo.integration.meeting;

import static org.assertj.core.api.Assertions.*;

import com.boardgo.common.exception.CustomIllegalArgumentException;
import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.entity.MeetingGameMatchEntity;
import com.boardgo.domain.meeting.entity.MeetingGenreMatchEntity;
import com.boardgo.domain.meeting.entity.MeetingType;
import com.boardgo.domain.meeting.repository.MeetingGameMatchRepository;
import com.boardgo.domain.meeting.repository.MeetingGenreMatchRepository;
import com.boardgo.domain.meeting.repository.MeetingRepository;
import com.boardgo.domain.meeting.service.MeetingCreateFactory;
import com.boardgo.integration.support.IntegrationTestSupport;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MeetingCreateFactoryTest extends IntegrationTestSupport {
    @Autowired private MeetingCreateFactory meetingCreateFactory;
    @Autowired private MeetingRepository meetingRepository;
    @Autowired private MeetingGameMatchRepository meetingGameMatchRepository;
    @Autowired private MeetingGenreMatchRepository meetingGenreMatchRepository;

    @Test
    @DisplayName("모임을 저장할 수 있다")
    void 모임을_저장할_수_있다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        MeetingEntity meetingEntity =
                MeetingEntity.builder()
                        .content("content")
                        .meetingDatetime(now)
                        .type(MeetingType.FREE)
                        .city("서울특별시")
                        .county("강남구")
                        .thumbnail("test.png")
                        .limitParticipant(5)
                        .longitude("32.12312412412")
                        .latitude("146.1232312321")
                        .build();
        List<Long> boardGameIdList = List.of(1L, 2L);
        List<Long> genreIdList = List.of(3L, 4L);
        // when
        Long meetingId = meetingCreateFactory.create(meetingEntity, boardGameIdList, genreIdList);
        // then
        MeetingEntity meeting = meetingRepository.findById(meetingId).get();
        assertThat(meeting.getMeetingDatetime()).isEqualTo(now);
        assertThat(meeting.getCity()).isEqualTo(meetingEntity.getCity());
        assertThat(meeting.getLatitude()).isEqualTo(meetingEntity.getLatitude());
        assertThat(meeting.getLongitude()).isEqualTo(meetingEntity.getLongitude());
        assertThat(meeting.getType()).isEqualTo(meetingEntity.getType());
        assertThat(meeting.getContent()).isEqualTo(meetingEntity.getContent());
        assertThat(meeting.getCounty()).isEqualTo(meetingEntity.getCounty());

        List<MeetingGameMatchEntity> gameMatchEntityList =
                meetingGameMatchRepository.findByMeetingId(meetingId);
        List<MeetingGenreMatchEntity> genreMatchEntityList =
                meetingGenreMatchRepository.findByMeetingId(meetingId);
        assertThat(gameMatchEntityList).extracting("boardGameId").contains(1L, 2L);
        assertThat(genreMatchEntityList).extracting("boardGameGenreId").contains(3L, 4L);
    }

    @Test
    @DisplayName("boardGameId가 Null인 경우 에러가 발생한다")
    void boardGameId가_Null인_경우_에러가_발생한다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        MeetingEntity meetingEntity =
                MeetingEntity.builder()
                        .content("content")
                        .meetingDatetime(now)
                        .type(MeetingType.FREE)
                        .city("서울특별시")
                        .county("강남구")
                        .thumbnail("test.png")
                        .limitParticipant(5)
                        .longitude("32.12312412412")
                        .latitude("146.1232312321")
                        .build();
        List<Long> genreIdList = List.of(3L, 4L);
        // when
        // then
        assertThatThrownBy(
                        () -> {
                            meetingCreateFactory.create(meetingEntity, null, genreIdList);
                        })
                .isInstanceOf(CustomIllegalArgumentException.class);
    }

    @Test
    @DisplayName("genreId가 Null인 경우 에러가 발생한다")
    void genreId가_Null인_경우_에러가_발생한다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        MeetingEntity meetingEntity =
                MeetingEntity.builder()
                        .content("content")
                        .meetingDatetime(now)
                        .type(MeetingType.FREE)
                        .city("서울특별시")
                        .county("강남구")
                        .thumbnail("test.png")
                        .limitParticipant(5)
                        .longitude("32.12312412412")
                        .latitude("146.1232312321")
                        .build();
        List<Long> boardGameIdList = List.of(1L, 2L);
        // when
        // then
        assertThatThrownBy(
                        () -> {
                            meetingCreateFactory.create(meetingEntity, boardGameIdList, null);
                        })
                .isInstanceOf(CustomIllegalArgumentException.class);
    }
}
