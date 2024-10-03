package com.boardgo.notification.repository;

import com.boardgo.notification.entity.QNotificationEntity;
import com.boardgo.notification.repository.projection.NotificationProjection;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationDslRepositoryImpl implements NotificationDslRepository {

    private final JPAQueryFactory queryFactory;
    private final QNotificationEntity n = QNotificationEntity.notificationEntity;

    public NotificationDslRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<NotificationProjection> findByUserInfoId(Long userId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                NotificationProjection.class,
                                n.id,
                                n.message.title,
                                n.message.content,
                                n.isRead,
                                n.pathUrl))
                .from(n)
                .where(n.isSent.eq(true).and(n.sendDateTime.before(LocalDateTime.now())))
                .orderBy(n.sendDateTime.desc())
                .fetch();
    }
}
