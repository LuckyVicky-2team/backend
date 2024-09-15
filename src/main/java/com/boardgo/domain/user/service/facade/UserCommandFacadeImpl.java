package com.boardgo.domain.user.service.facade;

import com.boardgo.common.exception.DuplicateException;
import com.boardgo.domain.user.controller.request.SignupRequest;
import com.boardgo.domain.user.controller.request.SocialSignupRequest;
import com.boardgo.domain.user.entity.UserInfoEntity;
import com.boardgo.domain.user.service.UserCommandUseCase;
import com.boardgo.domain.user.service.UserPrTagCommandUseCase;
import com.boardgo.domain.user.service.UserQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCommandFacadeImpl implements UserCommandFacade {

    private final UserCommandUseCase userCommandUseCase;
    private final UserQueryUseCase userQueryUseCase;
    private final UserPrTagCommandUseCase userPrTagCommandUseCase;

    @Override
    public Long signup(SignupRequest signupRequest) {
        userCommandUseCase.validateNickNameAndPrTag(
                signupRequest.nickName(), signupRequest.prTags());
        // TODO. 약관동의 저장
        Long userId = userCommandUseCase.save(signupRequest);
        userPrTagCommandUseCase.bulkInsertPrTags(signupRequest.prTags(), userId);
        return userId;
    }

    @Override
    public Long socialSignup(SocialSignupRequest signupRequest, Long userId) {
        UserInfoEntity userInfoEntity = userQueryUseCase.getUserInfoEntity(userId);
        if (userQueryUseCase.existNickName(signupRequest.nickName())) {
            throw new DuplicateException("중복된 닉네임입니다.");
        }
        userCommandUseCase.validateNickNameAndPrTag(
                signupRequest.nickName(), signupRequest.prTags());
        // TODO. 약관동의 저장
        userInfoEntity.updateNickname(signupRequest.nickName());
        userPrTagCommandUseCase.bulkInsertPrTags(signupRequest.prTags(), userInfoEntity.getId());
        return userInfoEntity.getId();
    }
}
