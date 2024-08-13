package com.boardgo.domain.boardgame.repository;

import com.boardgo.domain.boardgame.controller.request.BoardGameSearchRequest;
import com.boardgo.domain.boardgame.entity.QBoardGameEntity;
import com.boardgo.domain.boardgame.entity.QBoardGameGenreEntity;
import com.boardgo.domain.boardgame.entity.QGameGenreMatchEntity;
import com.boardgo.domain.boardgame.repository.projection.BoardGameSearchProjection;
import com.boardgo.domain.boardgame.repository.response.BoardGameSearchResponse;
import com.boardgo.domain.mapper.BoardGameMapper;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class BoardGameDslRepositoryImpl implements BoardGameDslRepository {

    private final JPAQueryFactory queryFactory;
    private final BoardGameMapper boardGameMapper;
    private final QBoardGameEntity b = QBoardGameEntity.boardGameEntity;
    private final QBoardGameGenreEntity bgg = QBoardGameGenreEntity.boardGameGenreEntity;
    private final QGameGenreMatchEntity ggm = QGameGenreMatchEntity.gameGenreMatchEntity;

    public BoardGameDslRepositoryImpl(
            EntityManager entityManager, BoardGameMapper boardGameMapper) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.boardGameMapper = boardGameMapper;
    }

    @Override
    public Page<BoardGameSearchResponse> findBySearchWord(BoardGameSearchRequest request) {
        int size = getSize(request.size());
        int page = getPage(request.page());
        int offset = page * size;

        List<BoardGameSearchProjection> queryResults =
                findBoardGameBySearchWord(request, size, offset);
        long total = countBySearchResult(request);

        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return new PageImpl<>(
                queryResults.stream().map(boardGameMapper::toBoardGameSearchResponse).toList(),
                pageable,
                total);
    }

    private List<BoardGameSearchProjection> findBoardGameBySearchWord(
            BoardGameSearchRequest request, int size, int offset) {
        return queryFactory
                .select(
                        Projections.constructor(
                                BoardGameSearchProjection.class,
                                b.id,
                                b.title,
                                b.thumbnail,
                                Expressions.stringTemplate("GROUP_CONCAT({0})", bgg.genre)
                                        .as("genres")))
                .from(b)
                .innerJoin(ggm)
                .on(b.id.eq(ggm.boardGameId))
                .innerJoin(bgg)
                .on(ggm.boardGameGenreId.eq(bgg.id))
                .where(searchKeyword(request.searchWord()))
                .groupBy(b.id)
                .offset(offset)
                .limit(size)
                .fetch();
    }

    private long countBySearchResult(BoardGameSearchRequest request) {
        if (Objects.nonNull(request.count())) {
            return request.count();
        } else {
            return queryFactory
                    .select(b.count())
                    .from(b)
                    .where(searchKeyword(request.searchWord()))
                    .fetchOne();
        }
    }

    private int getPage(Integer page) {
        return Objects.nonNull(page) ? page : 0;
    }

    private int getSize(Integer size) {
        return Objects.nonNull(size) ? size : 5;
    }

    private BooleanExpression searchKeyword(String searchWord) {
        return Objects.nonNull(searchWord)
                ? b.title.toLowerCase().contains(searchWord.toLowerCase())
                : null;
    }
}
