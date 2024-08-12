package com.boardgo.domain.user.service;

import static com.boardgo.common.exception.advice.dto.ErrorCode.DUPLICATE_DATA;
import static com.boardgo.common.utils.CustomStringUtils.existString;
import static com.boardgo.common.utils.ValidateUtils.validateNickname;
import static com.boardgo.common.utils.ValidateUtils.validatePrTag;

import com.boardgo.common.exception.CustomIllegalArgumentException;
import com.boardgo.common.exception.CustomNullPointException;
import com.boardgo.common.utils.FileUtils;
import com.boardgo.common.utils.S3Service;
import com.boardgo.domain.mapper.UserInfoMapper;
import com.boardgo.domain.user.controller.dto.SignupRequest;
import com.boardgo.domain.user.controller.dto.SocialSignupRequest;
import com.boardgo.domain.user.controller.dto.UserPersonalInfoUpdateRequest;
import com.boardgo.domain.user.entity.UserInfoEntity;
import com.boardgo.domain.user.entity.UserPrTagEntity;
import com.boardgo.domain.user.repository.UserPrTagRepository;
import com.boardgo.domain.user.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandCommandServiceV1 implements UserCommandUseCase {
    private final UserRepository userRepository;
    private final UserPrTagRepository userPrTagRepository;
    private final UserInfoMapper userInfoMapper;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    @Override
    public Long signup(SignupRequest signupRequest) {
        UserInfoEntity userInfo = userInfoMapper.toUserInfoEntity(signupRequest);
        userInfo.encodePassword(passwordEncoder);
        UserInfoEntity savedUser = userRepository.save(userInfo);
        userPrTagRepository.bulkInsertPrTags(signupRequest.prTags(), savedUser.getId());
        return savedUser.getId();
    }

    @Override
    public Long socialSignup(SocialSignupRequest signupRequest, Long userId) {
        UserInfoEntity userInfoEntity = getUserInfoEntity(userId);
        if (userRepository.existsByNickName(signupRequest.nickName())) {
            throw new CustomIllegalArgumentException(DUPLICATE_DATA.getCode(), "중복된 닉네임입니다.");
        }
        validateNickname(signupRequest.nickName());

        List<String> prTagList = signupRequest.prTags();
        if (!Objects.isNull(prTagList)) {
            validatePrTag(prTagList);
        }

        userInfoEntity.updateNickname(signupRequest.nickName());
        userPrTagRepository.bulkInsertPrTags(prTagList, userInfoEntity.getId());
        return userInfoEntity.getId();
    }

    @Transactional
    @Override
    public void updatePersonalInfo(
            Long userId, UserPersonalInfoUpdateRequest updateRequest, MultipartFile profileImage) {
        UserInfoEntity userInfoEntity = getUserInfoEntity(userId);

        if (!profileImage.isEmpty()) {
            s3Service.upload(FileUtils.getUniqueFileName(profileImage), profileImage);
        }

        if (existString(updateRequest.nickName()) && validateNickname(updateRequest.nickName())) {
            userInfoEntity.updateNickname(updateRequest.nickName());
        }
        userInfoEntity.encodePassword(passwordEncoder);
    }

    @Override
    public void updatePrTags(List<String> changedPrTag, Long userId) {
        List<UserPrTagEntity> prTags = userPrTagRepository.findByUserInfoId(userId);
        userPrTagRepository.deleteAllInBatch(prTags);
        userPrTagRepository.bulkInsertPrTags(changedPrTag, userId);
    }

    private UserInfoEntity getUserInfoEntity(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new CustomNullPointException("회원이 존재하지 않습니다"));
    }
}
