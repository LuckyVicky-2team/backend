package com.boardgo.domain.user.service;

import com.boardgo.common.exception.AlreadyElementExistException;
import com.boardgo.domain.user.controller.dto.EmailRequest;
import com.boardgo.domain.user.controller.dto.NickNameRequest;
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
    public void existEmail(EmailRequest emailRequest) {
        if (userRepository.existsByEmail(emailRequest.email())) {
            throw new AlreadyElementExistException("email 중복");
        }
    }

    @Override
    public void existNickName(NickNameRequest nickNameRequest) {
        if (userRepository.existsByNickName(nickNameRequest.nickName())) {
            throw new AlreadyElementExistException("nickName 중복");
        }
    }
}
