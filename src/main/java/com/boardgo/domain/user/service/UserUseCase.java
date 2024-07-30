package com.boardgo.domain.user.service;

import com.boardgo.domain.user.controller.dto.UserResponse;
import com.boardgo.domain.user.entity.UserEntity;

public interface UserUseCase {
    void save(UserEntity userEntity);
    UserEntity selectAll();
    void saveDsl(UserEntity userEntity);
    UserResponse selectAllDsl();
}
