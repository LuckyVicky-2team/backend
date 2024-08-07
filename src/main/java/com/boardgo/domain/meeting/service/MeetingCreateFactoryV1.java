package com.boardgo.domain.meeting.service;

import com.boardgo.domain.boardgame.repository.BoardGameGenreRepository;
import com.boardgo.domain.boardgame.repository.BoardGameRepository;
import com.boardgo.domain.meeting.entity.MeetingEntity;
import com.boardgo.domain.meeting.repository.MeetingGameMatchRepository;
import com.boardgo.domain.meeting.repository.MeetingGenreMatchRepository;
import com.boardgo.domain.meeting.repository.MeetingRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingCreateFactoryV1 implements MeetingCreateFactory {
    private final MeetingRepository meetingRepository;
    private final BoardGameGenreRepository boardGameGenreRepository;
    private final BoardGameRepository boardGameRepository;
    private final MeetingGenreMatchRepository meetingGenreMatchRepository;
    private final MeetingGameMatchRepository meetingGameMatchRepository;

    public Long create(MeetingEntity meeting, List<Long> boardGameIdList, List<Long> genreIdList) {
        // 1. meeting 저장
        Long meetingId = meetingRepository.save(meeting).getId();
        // 2. game_meeting_match bulk insert, meeting_genre_match bulk insert
        meetingGenreMatchRepository.bulkInsert(genreIdList, meetingId);
        meetingGameMatchRepository.bulkInsert(boardGameIdList, meetingId);
        return meetingId;
    }
}
