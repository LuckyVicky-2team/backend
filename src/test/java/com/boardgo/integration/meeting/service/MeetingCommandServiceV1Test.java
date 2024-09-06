package com.boardgo.integration.meeting.service;

import static com.boardgo.domain.meeting.entity.enums.MeetingState.*;
import static com.boardgo.integration.data.MeetingData.*;
import static com.boardgo.integration.data.UserInfoData.*;
import static com.boardgo.integration.fixture.MeetingParticipantFixture.*;
import static org.assertj.core.api.Assertions.*;

import com.boardgo.common.exception.CustomIllegalArgumentException;
import com.boardgo.domain.meeting.controller.request.MeetingUpdateRequest;
import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.entity.MeetingGameMatchEntity;
import com.boardgo.domain.meeting.entity.MeetingGenreMatchEntity;
import com.boardgo.domain.meeting.entity.MeetingLikeEntity;
import com.boardgo.domain.meeting.entity.MeetingParticipantEntity;
import com.boardgo.domain.meeting.entity.enums.MeetingType;
import com.boardgo.domain.meeting.entity.enums.ParticipantType;
import com.boardgo.domain.meeting.repository.MeetingGameMatchRepository;
import com.boardgo.domain.meeting.repository.MeetingGenreMatchRepository;
import com.boardgo.domain.meeting.repository.MeetingLikeRepository;
import com.boardgo.domain.meeting.repository.MeetingParticipantRepository;
import com.boardgo.domain.meeting.repository.MeetingRepository;
import com.boardgo.domain.meeting.service.MeetingCommandUseCase;
import com.boardgo.domain.meeting.service.MeetingCreateFactory;
import com.boardgo.domain.user.entity.UserInfoEntity;
import com.boardgo.domain.user.repository.UserRepository;
import com.boardgo.domain.user.service.response.CustomUserDetails;
import com.boardgo.integration.init.TestBoardGameInitializer;
import com.boardgo.integration.init.TestUserInfoInitializer;
import com.boardgo.integration.support.IntegrationTestSupport;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class MeetingCommandServiceV1Test extends IntegrationTestSupport {
    @Autowired private MeetingRepository meetingRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private MeetingParticipantRepository meetingParticipantRepository;
    @Autowired private MeetingCommandUseCase meetingCommandUseCase;
    @Autowired private MeetingLikeRepository meetingLikeRepository;
    @Autowired private MeetingGenreMatchRepository meetingGenreMatchRepository;
    @Autowired private MeetingGameMatchRepository meetingGameMatchRepository;
    @Autowired private EntityManager entityManager;
    @Autowired private MeetingCreateFactory meetingCreateFactory;
    @Autowired private TestUserInfoInitializer testUserInfoInitializer;
    @Autowired private TestBoardGameInitializer testBoardGameInitializer;

    @Test
    @DisplayName("모임을 수정할 수 있다")
    void 모임을_수정할_수_있다() {
        // given
        LocalDateTime meetingDatetime = LocalDateTime.now().plusDays(1);
        long userId = 1L;
        MeetingEntity meetingEntity =
                MeetingEntity.builder()
                        .viewCount(0L)
                        .userId(userId)
                        .latitude("12312312")
                        .longitude("12321")
                        .thumbnail("thumbnail")
                        .state(PROGRESS)
                        .meetingDatetime(meetingDatetime)
                        .type(MeetingType.FREE)
                        .content("content")
                        .city("city")
                        .county("county")
                        .title("title")
                        .locationName("location")
                        .detailAddress("detailAddress")
                        .limitParticipant(5)
                        .build();
        List<Long> boardGameIdList = List.of(1L, 2L);
        List<Long> boardGameGenreIdList = List.of(1L, 2L);
        Long meetingId =
                meetingCreateFactory.create(meetingEntity, boardGameIdList, boardGameGenreIdList);
        LocalDateTime updatedMeetingDatetime = meetingDatetime.plusDays(2);
        MeetingUpdateRequest meetingUpdateRequest =
                new MeetingUpdateRequest(
                        meetingId,
                        "updateContent",
                        "FREE",
                        4,
                        "updatedTitle",
                        "updateCity",
                        "updateCounty",
                        "35.12321312",
                        "1232.213213213",
                        "updateAddress",
                        "updateLocation",
                        updatedMeetingDatetime,
                        List.of(3L, 4L),
                        List.of(3L, 4L));
        MockMultipartFile mockFile =
                new MockMultipartFile(
                        "file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
        // when
        meetingCommandUseCase.updateMeeting(meetingUpdateRequest, userId, mockFile);
        // then
        MeetingEntity updateMeeting = meetingRepository.findById(meetingId).get();
        assertThat(updateMeeting).isNotNull();
        assertThat(updateMeeting.getMeetingDatetime()).isEqualTo(updatedMeetingDatetime);
        assertThat(updateMeeting.getType()).isEqualTo(MeetingType.FREE);
        assertThat(updateMeeting.getContent()).isEqualTo(meetingUpdateRequest.content());
        assertThat(updateMeeting.getCity()).isEqualTo(meetingUpdateRequest.city());
        assertThat(updateMeeting.getCounty()).isEqualTo(meetingUpdateRequest.county());
        assertThat(updateMeeting.getTitle()).isEqualTo(meetingUpdateRequest.title());
        assertThat(updateMeeting.getLocationName()).isEqualTo(meetingUpdateRequest.locationName());
        assertThat(updateMeeting.getDetailAddress())
                .isEqualTo(meetingUpdateRequest.detailAddress());
        assertThat(updateMeeting.getLimitParticipant())
                .isEqualTo(meetingUpdateRequest.limitParticipant());
        assertThat(updateMeeting.getLongitude()).isEqualTo(meetingUpdateRequest.longitude());
        assertThat(updateMeeting.getLatitude()).isEqualTo(meetingUpdateRequest.latitude());
        assertThat(updateMeeting.getThumbnail()).isNotNull();
        assertThat(updateMeeting.getState()).isEqualTo(PROGRESS);
        List<MeetingGameMatchEntity> gameMatchEntityList =
                meetingGameMatchRepository.findByMeetingId(meetingId);
        assertThat(gameMatchEntityList).isNotNull();
        assertThat(gameMatchEntityList.size()).isEqualTo(2);
        assertThat(gameMatchEntityList)
                .extracting(MeetingGameMatchEntity::getBoardGameId)
                .contains(3L, 4L);
    }

    @Test
    @DisplayName("현재 참가한 인원보다 최대 인원을 줄일 수가 없다")
    void 현재_참가한_인원보다_최대_인원을_줄일_수가_없다() {
        // given
        LocalDateTime meetingDatetime = LocalDateTime.now().plusDays(1);
        long userId = 1L;
        MeetingEntity meetingEntity =
                MeetingEntity.builder()
                        .viewCount(0L)
                        .userId(userId)
                        .latitude("12312312")
                        .longitude("12321")
                        .thumbnail("thumbnail")
                        .state(PROGRESS)
                        .meetingDatetime(meetingDatetime)
                        .type(MeetingType.FREE)
                        .content("content")
                        .city("city")
                        .county("county")
                        .title("title")
                        .locationName("location")
                        .detailAddress("detailAddress")
                        .limitParticipant(5)
                        .build();

        List<Long> boardGameIdList = List.of(1L, 2L);
        List<Long> boardGameGenreIdList = List.of(1L, 2L);
        Long meetingId =
                meetingCreateFactory.create(meetingEntity, boardGameIdList, boardGameGenreIdList);
        meetingParticipantRepository.save(
                MeetingParticipantEntity.builder()
                        .meetingId(meetingId)
                        .userInfoId(3L)
                        .type(ParticipantType.PARTICIPANT)
                        .build());
        meetingParticipantRepository.save(
                MeetingParticipantEntity.builder()
                        .meetingId(meetingId)
                        .userInfoId(4L)
                        .type(ParticipantType.PARTICIPANT)
                        .build());
        meetingParticipantRepository.save(
                MeetingParticipantEntity.builder()
                        .meetingId(meetingId)
                        .userInfoId(5L)
                        .type(ParticipantType.PARTICIPANT)
                        .build());
        meetingParticipantRepository.save(
                MeetingParticipantEntity.builder()
                        .meetingId(meetingId)
                        .userInfoId(6L)
                        .type(ParticipantType.PARTICIPANT)
                        .build());
        LocalDateTime updatedMeetingDatetime = meetingDatetime.plusDays(2);
        MeetingUpdateRequest meetingUpdateRequest =
                new MeetingUpdateRequest(
                        meetingId,
                        "updateContent",
                        "FREE",
                        2,
                        "updatedTitle",
                        "updateCity",
                        "updateCounty",
                        "35.12321312",
                        "1232.213213213",
                        "updateAddress",
                        "updateLocation",
                        updatedMeetingDatetime,
                        List.of(3L, 4L),
                        List.of(3L, 4L));
        MockMultipartFile mockFile =
                new MockMultipartFile(
                        "file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
        // when
        // then
        assertThatThrownBy(
                        () -> {
                            meetingCommandUseCase.updateMeeting(
                                    meetingUpdateRequest, userId, mockFile);
                        })
                .isInstanceOf(CustomIllegalArgumentException.class)
                .message()
                .isEqualTo("현재 참여한 인원보다 최대 인원수 보다 커야합니다.");
    }

    @Test
    @DisplayName("모임을 삭제할 수 있다")
    void 모임을_삭제할_수_있다() {
        // given
        LocalDateTime meetingDatetime = LocalDateTime.now().plusDays(1);
        long userId = 1L;
        MeetingEntity meetingEntity =
                MeetingEntity.builder()
                        .viewCount(0L)
                        .userId(userId)
                        .latitude("12312312")
                        .longitude("12321")
                        .thumbnail("thumbnail")
                        .state(PROGRESS)
                        .meetingDatetime(meetingDatetime)
                        .type(MeetingType.FREE)
                        .content("content")
                        .city("city")
                        .county("county")
                        .title("title")
                        .locationName("location")
                        .detailAddress("detailAddress")
                        .limitParticipant(5)
                        .build();
        List<Long> boardGameIdList = List.of(userId, 2L);
        List<Long> boardGameGenreIdList = List.of(userId, 2L);
        Long meetingId =
                meetingCreateFactory.create(meetingEntity, boardGameIdList, boardGameGenreIdList);
        meetingParticipantRepository.save(
                MeetingParticipantEntity.builder()
                        .meetingId(meetingId)
                        .userInfoId(2L)
                        .type(ParticipantType.PARTICIPANT)
                        .build());

        // when
        meetingCommandUseCase.deleteMeeting(meetingId, userId);
        // then
        Optional<MeetingEntity> meetingOptional = meetingRepository.findById(meetingId);
        List<MeetingParticipantEntity> meetingParticipantList =
                meetingParticipantRepository.findByMeetingId(meetingId);
        List<MeetingLikeEntity> meetingLikeEntityList =
                meetingLikeRepository.findByMeetingId(meetingId);
        List<MeetingGenreMatchEntity> genreMatchList =
                meetingGenreMatchRepository.findByMeetingId(meetingId);
        List<MeetingGameMatchEntity> gameMatchEntityList =
                meetingGameMatchRepository.findByMeetingId(meetingId);

        assertThat(meetingOptional).isEmpty();
        assertThat(meetingParticipantList.isEmpty()).isTrue();
        assertThat(meetingLikeEntityList.isEmpty()).isTrue();
        assertThat(genreMatchList.isEmpty()).isTrue();
        assertThat(gameMatchEntityList.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("모임 상세 조회 시 조회수가 오른다")
    void 모임_상세_조회_시_조회수가_오른다() {
        // given
        testBoardGameInitializer.generateBoardGameData();
        setSecurityContext();

        LocalDateTime meetingDatetime = LocalDateTime.now().plusDays(1);
        MeetingEntity meetingEntity =
                MeetingEntity.builder()
                        .viewCount(0L)
                        .userId(1L)
                        .latitude("12312312")
                        .longitude("12321")
                        .thumbnail("thumbnail")
                        .state(COMPLETE)
                        .meetingDatetime(meetingDatetime)
                        .type(MeetingType.FREE)
                        .content("content")
                        .city("city")
                        .county("county")
                        .title("title")
                        .locationName("location")
                        .detailAddress("detailAddress")
                        .limitParticipant(5)
                        .build();
        List<Long> boardGameIdList = List.of(1L, 2L);
        List<Long> boardGameGenreIdList = List.of(1L, 2L);
        Long meetingId =
                meetingCreateFactory.create(meetingEntity, boardGameIdList, boardGameGenreIdList);

        MeetingParticipantEntity savedParticipant =
                meetingParticipantRepository.save(
                        MeetingParticipantEntity.builder()
                                .meetingId(meetingId)
                                .userInfoId(2L)
                                .type(ParticipantType.PARTICIPANT)
                                .build());

        // when
        // then
        meetingCommandUseCase.incrementViewCount(meetingId);
        MeetingEntity result = meetingRepository.findById(meetingId).get();
        assertThat(result.getViewCount()).isEqualTo(1L);
        meetingCommandUseCase.incrementViewCount(meetingId);
        MeetingEntity result2 = meetingRepository.findById(meetingId).get();
        assertThat(result2.getViewCount()).isEqualTo(2L);
    }

    @Test
    @DisplayName("공유하면 공유 횟수를 증가시킬 수 있다")
    void 공유하면_공유_횟수를_증가시킬_수_있다() {
        // given
        MeetingEntity meetingEntity = getMeetingEntityData(1L).limitParticipant(10).build();
        MeetingEntity savedMeeting = meetingRepository.save(meetingEntity);
        entityManager.clear();
        // when
        meetingCommandUseCase.incrementShareCount(savedMeeting.getId());
        // then
        MeetingEntity meeting = meetingRepository.findById(savedMeeting.getId()).get();
        System.out.println("meeting = " + meeting);
        assertThat(meeting.getShareCount()).isEqualTo(1L);
    }

    private void setSecurityContext() {
        testUserInfoInitializer.generateUserData();
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserInfoEntity userInfoEntity =
                userRepository
                        .findById(1L)
                        .orElseThrow(() -> new RuntimeException("User not found"));
        CustomUserDetails customUserDetails = new CustomUserDetails(userInfoEntity);

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        customUserDetails, "password1", customUserDetails.getAuthorities());
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
    }

    @Test
    @DisplayName("모임의 인원이 정원인 경우 모임 완료 처리 된다")
    void 모임의_인원이_정원인_경우_모임_완료_처리_된다() {
        // given
        List<Long> meetingIds = new ArrayList<>();
        int limit = 5;
        long leader = 1L;
        for (int i = 0; i < 10; i++) {
            userRepository.save(userInfoEntityData(i + "email@naver.com", "내이름" + i).build());
            MeetingEntity meeting = getMeetingEntityData(leader).limitParticipant(limit).build();
            meetingIds.add(meetingRepository.save(meeting).getId());
            meetingParticipantRepository.save(
                    getLeaderMeetingParticipantEntity(meeting.getId(), leader));
        }

        // when: 모임정원
        for (int i = 1; i < limit; i++) {
            for (int j = i; j < limit; j++) {
                meetingParticipantRepository.save(
                        getParticipantMeetingParticipantEntity((long) i, (long) j + 1));
            }
            meetingCommandUseCase.updateCompleteMeetingState((long) i);
        }

        // then
        List<MeetingEntity> meetingEntities = meetingRepository.findByIdIn(meetingIds);
        int index = 0;
        while (limit == index) {
            MeetingEntity meetingEntity = meetingEntities.get(index);
            assertThat(meetingEntity.getState().toString()).isEqualTo(COMPLETE.toString());
        }
    }
}
