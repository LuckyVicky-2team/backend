package com.boardgo.integration.user.service;

import com.boardgo.domain.user.controller.dto.EmailRequest;
import com.boardgo.domain.user.entity.UserInfoEntity;
import com.boardgo.domain.user.repository.UserRepository;
import com.boardgo.domain.user.service.UserQueryUseCase;
import com.boardgo.integration.support.IntegrationTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserQueryServiceV1Test extends IntegrationTestSupport {
    @Autowired private UserRepository userRepository;
    @Autowired private UserQueryUseCase userQueryUseCase;

    @Test
    @DisplayName("해당 이메일이 존재하지 않으면 false를 반환한다")
    void 해당_이메일이_존재하지_않으면_false를_반환한다() {
        // given
        EmailRequest emailRequest = new EmailRequest("aa@aa.com");
        // when
        boolean result = userQueryUseCase.existEmail(emailRequest);
        // then
        Assertions.assertThat(result).isEqualTo(false);
    }

    @Test
    @DisplayName("해당 이메일이 존재하면 true를 반환한다")
    void 해당_이메일이_존재하면_true를_반환한다() {
        // given
        EmailRequest emailRequest = new EmailRequest("aa@aa.com");
        UserInfoEntity userInfoEntity =
                UserInfoEntity.builder()
                        .email("aa@aa.com")
                        .password("password")
                        .nickName("nickName")
                        .build();
        userRepository.save(userInfoEntity);
        // when
        boolean result = userQueryUseCase.existEmail(emailRequest);
        // then
        Assertions.assertThat(result).isEqualTo(true);
    }
}
