package com.boardgo.domain.user.service;

import com.boardgo.domain.user.controller.request.EmailRequest;
import com.boardgo.domain.user.controller.request.NickNameRequest;
import com.boardgo.domain.user.entity.UserInfoEntity;
import com.boardgo.domain.user.service.response.OtherPersonalInfoResponse;
import com.boardgo.domain.user.service.response.UserPersonalInfoResponse;

public interface UserQueryUseCase {

    UserInfoEntity getById(Long id);

    void existEmail(EmailRequest emailRequest);

    void existNickName(NickNameRequest nickNameRequest);

    UserPersonalInfoResponse getPersonalInfo(Long userId);

    OtherPersonalInfoResponse getOtherPersonalInfo(Long userId);
}
