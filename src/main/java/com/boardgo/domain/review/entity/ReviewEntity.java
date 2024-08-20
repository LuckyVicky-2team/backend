package com.boardgo.domain.review.entity;

import com.boardgo.common.converter.BooleanConverter;
import com.boardgo.common.converter.DelimiterConverter;
import com.boardgo.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Table(
        name = "review",
        indexes = {
            @Index(name = "idx_user_info_id", columnList = "user_info_id"),
            @Index(name = "idx_reviewer_id", columnList = "reviewer_id")
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewEntity extends BaseEntity {
    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_info_id", nullable = false)
    private Long userInfoId;

    @Column(name = "reviewer_id", nullable = false)
    private Long reviewerId;

    @Column(name = "meeting_id", nullable = false)
    private Long meetingId;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private Integer rating;

    @Convert(converter = DelimiterConverter.class)
    private List<String> evaluationTagList = new ArrayList<>();

    @Convert(converter = BooleanConverter.class)
    @ColumnDefault("N")
    private boolean idDeleted;
}
