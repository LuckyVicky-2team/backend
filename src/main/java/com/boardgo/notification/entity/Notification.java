package com.boardgo.notification.entity;

import com.boardgo.common.converter.BooleanConverter;
import com.boardgo.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(
        name = "notification",
        indexes = {@Index(name = "idx_user_info_id", columnList = "user_info_id")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Comment("읽음 유무")
    @Convert(converter = BooleanConverter.class)
    @Column(columnDefinition = "varchar(1)")
    private Boolean isRead;

    @Comment("받는 사람")
    @Column(nullable = false)
    private Long userInfoId;

    @Comment("이동 경로")
    private String pathUrl;

    @Comment("발송 시간")
    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime sendTime;

    @Embedded private NotificationMessage message;

    @Builder
    private Notification(
            Boolean isRead,
            Long userInfoId,
            String pathUrl,
            LocalDateTime sendTime,
            NotificationMessage message) {
        this.isRead = isRead;
        this.userInfoId = userInfoId;
        this.pathUrl = pathUrl;
        this.sendTime = sendTime;
        this.message = message;
    }
}
