package com.boardgo.integration.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.boardgo.domain.user.controller.dto.EmailRequest;
import com.boardgo.domain.user.repository.UserRepository;
import com.boardgo.integration.support.IntegrationTestSupport;

public class UserQueryServiceV1Test extends IntegrationTestSupport {
	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("해당 이메일이 존재하지 않으면 false를 반환한다")
	void 해당_이메일이_존재하지_않으면_false를_반환한다() {
	    //given
	    new EmailRequest("")
	    //when

	    //then

	}
}
