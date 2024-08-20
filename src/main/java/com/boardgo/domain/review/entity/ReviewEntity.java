package com.boardgo.domain.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewEntity {
    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_info_id")
    private Long userInfoId;

    @Column(name = "meeting_id")
    private Long meetingId;

    private Integer grade;

    //    @ManyToOne(fetch = FetchType.LAZY)
    //    @JoinColumn(name = "evaluation_tag_id")
    //    private EvaluationTagEntity evaluationTag;
}
