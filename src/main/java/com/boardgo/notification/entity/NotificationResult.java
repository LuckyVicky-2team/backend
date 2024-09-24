package com.boardgo.notification.entity;

import com.boardgo.common.converter.BooleanConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(
        name = "notification_result",
        indexes = {@Index(name = "idx_notification_id", columnList = "notification_id")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_result_id")
    private Long id;

    @Column(nullable = false)
    private Long notificationId;

    @Comment("발송 유무")
    @Convert(converter = BooleanConverter.class)
    @Column(columnDefinition = "varchar(1)")
    private Boolean isSent;

    @Comment("성공 유무")
    @Convert(converter = BooleanConverter.class)
    @Column(columnDefinition = "varchar(1)")
    private Boolean isSuccessful;

    @Comment("발송 재시도 횟수")
    @Column(columnDefinition = "integer default 0")
    private Integer resendCount;

    @Builder
    private NotificationResult(
            Long notificationId, Boolean isSent, Boolean isSuccessful, Integer resendCount) {
        this.notificationId = notificationId;
        this.isSent = isSent;
        this.isSuccessful = isSuccessful;
        this.resendCount = resendCount;
    }
}
