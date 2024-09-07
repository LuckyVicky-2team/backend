package com.boardgo.domain.termsconditions.entity;

import com.boardgo.domain.termsconditions.entity.enums.TermsConditionsType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "user_terms_conditions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTermsConditionsEntity {

    @Id
    @Column(name = "user_terms_conditions_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("회원 고유 id")
    @Column(name = "user_info_id", nullable = false)
    private Long userInfoId;

    @Comment("약관동의 타입")
    @Enumerated(EnumType.STRING)
    @Column(name = "terms_conditions_type", length = 20, nullable = false)
    private TermsConditionsType termsConditionsType;

    public UserTermsConditionsEntity(Long userInfoId, TermsConditionsType termsConditionsType) {
        this.userInfoId = userInfoId;
        this.termsConditionsType = termsConditionsType;
    }
}
