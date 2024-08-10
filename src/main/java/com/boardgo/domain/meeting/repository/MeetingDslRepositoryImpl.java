package com.boardgo.domain.meeting.repository;

import com.boardgo.domain.boardgame.entity.QBoardGameEntity;
import com.boardgo.domain.boardgame.entity.QBoardGameGenreEntity;
import com.boardgo.domain.meeting.controller.dto.MeetingSearchRequest;
import com.boardgo.domain.meeting.entity.MeetingState;
import com.boardgo.domain.meeting.entity.QMeetingEntity;
import com.boardgo.domain.meeting.entity.QMeetingGameMatchEntity;
import com.boardgo.domain.meeting.entity.QMeetingGenreMatchEntity;
import com.boardgo.domain.meeting.entity.QMeetingParticipantEntity;
import com.boardgo.domain.meeting.repository.dto.MeetingSearchDto;
import com.boardgo.domain.user.entity.QUserInfoEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class MeetingDslRepositoryImpl implements MeetingDslRepository {
    private final JPAQueryFactory queryFactory;

    public MeetingDslRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<MeetingSearchDto> findByFilters(MeetingSearchRequest searchRequest) {
        QMeetingEntity m = QMeetingEntity.meetingEntity;
        QUserInfoEntity u = QUserInfoEntity.userInfoEntity;
        QMeetingParticipantEntity mp = QMeetingParticipantEntity.meetingParticipantEntity;
        QMeetingGameMatchEntity mg = QMeetingGameMatchEntity.meetingGameMatchEntity;
        QBoardGameEntity b = QBoardGameEntity.boardGameEntity;
        QMeetingGenreMatchEntity mg2 = QMeetingGenreMatchEntity.meetingGenreMatchEntity;
        QBoardGameGenreEntity g = QBoardGameGenreEntity.boardGameGenreEntity;

        MeetingState finishState = MeetingState.valueOf("FINISH");

        BooleanBuilder builder = new BooleanBuilder();

        // 동적 조건 추가 메서드 호출
        builder.and(genreFilter(searchRequest.genre(), g))
                .and(meetingDateBetween(searchRequest.startDate(), searchRequest.endDate(), m))
                .and(searchKeyword(searchRequest.searchWord(), searchRequest.searchType(), m))
                .and(cityFilter(searchRequest.city(), m))
                .and(countyFilter(searchRequest.county(), m));

        // 페이지네이션 처리
        int offset = searchRequest.page() * searchRequest.size();
        int limit = searchRequest.size();

        // 동적 정렬 조건 설정
        OrderSpecifier<?> sortOrder = getSortOrder(searchRequest.sortBy(), m, mp);

        List<MeetingSearchDto> results =
                queryFactory
                        .select(
                                Projections.constructor(
                                        MeetingSearchDto.class,
                                        m.id,
                                        m.title,
                                        m.city,
                                        m.county,
                                        m.meetingDatetime,
                                        m.limitParticipant,
                                        u.nickName,
                                        Expressions.stringTemplate("GROUP_CONCAT({0})", b.title)
                                                .as("games"), // GROUP_CONCAT for games
                                        Expressions.stringTemplate("GROUP_CONCAT({0})", g.genre)
                                                .as("genres"), // GROUP_CONCAT for genres
                                        mp.id.count()))
                        .from(m)
                        .join(u)
                        .on(u.id.eq(m.userId))
                        .join(mp)
                        .on(mp.meetingId.eq(m.id))
                        .join(mg)
                        .on(mg.meetingId.eq(m.id))
                        .join(b)
                        .on(b.id.eq(mg.boardGameId))
                        .join(mg2)
                        .on(mg2.meetingId.eq(m.id))
                        .join(g)
                        .on(g.id.eq(mg2.boardGameGenreId))
                        .where(m.state.ne(finishState).and(builder))
                        .groupBy(m.id)
                        .orderBy(sortOrder)
                        .offset(offset)
                        .limit(limit)
                        .fetch();
        long total;
        // 총 결과 수 계산
        if (Objects.isNull(searchRequest.count())) {
            total =
                    queryFactory
                            .select(m.id.count())
                            .from(m)
                            .join(u)
                            .on(u.id.eq(m.userId))
                            .join(mg)
                            .on(mg.meetingId.eq(m.id))
                            .join(b)
                            .on(b.id.eq(mg.boardGameId))
                            .join(mg2)
                            .on(mg2.meetingId.eq(m.id))
                            .join(g)
                            .on(g.id.eq(mg2.boardGameGenreId))
                            .where(m.state.ne(finishState).and(builder))
                            .fetchOne();
        } else {
            total = searchRequest.count();
        }

        Pageable pageable = Pageable.ofSize(searchRequest.size()).withPage(searchRequest.page());
        return new PageImpl<>(results, pageable, total);
    }

    // 동적 조건 메서드들
    private BooleanExpression genreFilter(String genreFilter, QBoardGameGenreEntity g) {
        return Objects.nonNull(genreFilter) ? g.genre.eq(genreFilter) : null;
    }

    private BooleanExpression meetingDateBetween(
            LocalDateTime startDate, LocalDateTime endDate, QMeetingEntity m) {
        return Objects.nonNull(startDate) && Objects.nonNull(endDate)
                ? m.meetingDatetime.between(startDate, endDate)
                : null;
    }

    private BooleanExpression searchKeyword(
            String searchWord, String searchType, QMeetingEntity m) {
        if (Objects.isNull(searchWord)) {
            return null;
        } else if (searchType.equals("TITLE")) {
            return m.title.contains(searchWord);
        } else if (searchType.equals("CONTENT")) {
            return m.content.contains(searchType);
        } else {
            return m.title.contains(searchWord).or(m.content.contains(searchType));
        }
    }

    private BooleanExpression cityFilter(String cityFilter, QMeetingEntity m) {
        return Objects.nonNull(cityFilter) ? m.city.eq(cityFilter) : null;
    }

    private BooleanExpression countyFilter(String countyFilter, QMeetingEntity m) {
        return Objects.nonNull(countyFilter) ? m.county.eq(countyFilter) : null;
    }

    // 동적 정렬 조건 메서드
    private OrderSpecifier<?> getSortOrder(
            String sortBy, QMeetingEntity m, QMeetingParticipantEntity mp) {
        if ("participantCount".equalsIgnoreCase(sortBy)) {
            return mp.id.count().desc(); // participantCount DESC
        } else {
            return m.meetingDatetime.asc(); // Default: meetingDate ASC
        }
    }
}
