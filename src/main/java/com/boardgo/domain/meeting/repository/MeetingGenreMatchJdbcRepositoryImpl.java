package com.boardgo.domain.meeting.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MeetingGenreMatchJdbcRepositoryImpl implements MeetingGenreMatchJdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void bulkInsert(List<Long> genreIdList, Long meetingId) {
        String sql =
                "INSERT IGNORE INTO meeting_genre_match (meeting_id, board_game_genre_id) VALUES (?, ?)";

        jdbcTemplate.batchUpdate(
                sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, meetingId);
                        ps.setLong(2, genreIdList.get(i));
                    }

                    @Override
                    public int getBatchSize() {
                        return genreIdList.size();
                    }
                });
    }
}
