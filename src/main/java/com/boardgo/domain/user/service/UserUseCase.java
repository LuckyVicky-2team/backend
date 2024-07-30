package com.boardgo.domain.user.service;

import com.boardgo.domain.user.entity.UserEntity;
import java.util.List;

public interface UserUseCase {
    void save(UserEntity userEntity);
    List<UserEntity> selectAll();
    void saveDsl(UserEntity userEntity);
    UserEntity selectDsl();
}
