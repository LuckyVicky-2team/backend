package com.boardgo.domain.review.repository;

import com.boardgo.domain.review.repository.projection.ReviewCountProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewDslRepositoryImpl implements ReviewDslRepository {

    private final JPAQueryFactory queryFactory;

    public ReviewDslRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public ReviewCountProjection countReviewByReviewerId(Long reviewerId) {
        return null;
    }
}
