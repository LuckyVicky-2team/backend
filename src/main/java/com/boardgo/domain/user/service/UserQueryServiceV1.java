package com.boardgo.domain.user.service;

import static com.boardgo.common.exception.advice.dto.ErrorCode.DUPLICATE_DATA;
import static com.boardgo.common.exception.advice.dto.ErrorCode.NULL_ERROR;

import com.boardgo.common.exception.CustomIllegalArgumentException;
import com.boardgo.common.exception.CustomNullPointException;
import com.boardgo.domain.mapper.UserInfoMapper;
import com.boardgo.domain.user.controller.dto.EmailRequest;
import com.boardgo.domain.user.controller.dto.NickNameRequest;
import com.boardgo.domain.user.controller.dto.UserPersonalInfoResponse;
import com.boardgo.domain.user.entity.ProviderType;
import com.boardgo.domain.user.entity.UserInfoEntity;
import com.boardgo.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryServiceV1 implements UserQueryUseCase {
    private final UserRepository userRepository;
    private final UserInfoMapper UserInfoMapper;

    @Override
    public void existEmail(EmailRequest emailRequest) {
        if (userRepository.existsByEmailAndProviderType(emailRequest.email(), ProviderType.LOCAL)) {
            throw new CustomIllegalArgumentException(DUPLICATE_DATA.getCode(), "중복된 Email입니다.");
        }
    }

    @Override
    public void existNickName(NickNameRequest nickNameRequest) {
        if (userRepository.existsByNickName(nickNameRequest.nickName())) {
            throw new CustomIllegalArgumentException(DUPLICATE_DATA.getCode(), "중복된 닉네임입니다.");
        }
    }

    @Override
    public UserPersonalInfoResponse getPersonalInfo(Long userId) {
        UserInfoEntity userInfoEntity =
                userRepository
                        .findById(userId)
                        .orElseThrow(
                                () ->
                                        new CustomNullPointException(
                                                NULL_ERROR.getCode(), "회원이 존재하지 않습니다"));
        // TODO. 평균별점
        Double averageGrade = 3.3;

        // TODO. PR태그 이름
        List<String> prTags = List.of("ENFJ", "빵순이", "까눌레", "마들렌");

        return UserInfoMapper.toUserPersonalInfoResponse(userInfoEntity, averageGrade, prTags);
    }
}
