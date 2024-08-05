package com.boardgo.domain.user.service;

import com.boardgo.domain.user.controller.dto.EmailRequest;

public interface UserQueryUseCase {
    boolean existEmail(EmailRequest emailRequest);
}
