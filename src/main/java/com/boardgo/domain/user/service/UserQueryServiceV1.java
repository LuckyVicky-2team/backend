package com.boardgo.domain.user.service;

import com.boardgo.domain.user.controller.dto.EmailRequest;
import com.boardgo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryServiceV1 implements UserQueryUseCase {
    private final UserRepository userRepository;

    @Override
    public boolean existEmail(EmailRequest emailRequest) {
        return userRepository.existsByEmail(emailRequest.email());
    }
}
