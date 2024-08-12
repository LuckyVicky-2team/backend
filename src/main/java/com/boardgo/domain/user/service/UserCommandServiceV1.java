package com.boardgo.domain.user.service;

import static com.boardgo.common.exception.advice.dto.ErrorCode.*;
import static com.boardgo.common.utils.ValidateUtils.*;

import com.boardgo.common.exception.CustomIllegalArgumentException;
import com.boardgo.common.exception.CustomNullPointException;
import com.boardgo.domain.mapper.UserInfoMapper;
import com.boardgo.domain.user.controller.dto.SignupRequest;
import com.boardgo.domain.user.controller.dto.SocialSignupRequest;
import com.boardgo.domain.user.entity.UserInfoEntity;
import com.boardgo.domain.user.repository.UserPrTagRepository;
import com.boardgo.domain.user.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandServiceV1 implements UserCommandUseCase {
    private final UserRepository userRepository;
    private final UserPrTagRepository userPrTagRepository;
    private final UserInfoMapper userInfoMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Long signup(SignupRequest signupRequest) {
        validateNickNameAndPrTag(signupRequest.nickName(), signupRequest.prTags());
        UserInfoEntity userInfo = userInfoMapper.toUserInfoEntity(signupRequest);
        userInfo.encodePassword(passwordEncoder);
        UserInfoEntity savedUser = userRepository.save(userInfo);
        userPrTagRepository.bulkInsertPrTags(signupRequest.prTags(), savedUser.getId());
        return savedUser.getId();
    }

    @Override
    public Long socialSignup(SocialSignupRequest signupRequest, Long userId) {
        UserInfoEntity userInfoEntity =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new CustomNullPointException("회원이 존재하지 않습니다"));

        if (userRepository.existsByNickName(signupRequest.nickName())) {
            throw new CustomIllegalArgumentException(DUPLICATE_DATA.getCode(), "중복된 닉네임입니다.");
        }
        validateNickNameAndPrTag(signupRequest.nickName(), signupRequest.prTags());

        userInfoEntity.updateNickname(signupRequest.nickName());
        userPrTagRepository.bulkInsertPrTags(signupRequest.prTags(), userInfoEntity.getId());
        return userInfoEntity.getId();
    }

    private void validateNickNameAndPrTag(String nickName, List<String> prTags) {
        validateNickname(nickName);

        if (!Objects.isNull(prTags)) {
            validatePrTag(prTags);
        }
    }
}
