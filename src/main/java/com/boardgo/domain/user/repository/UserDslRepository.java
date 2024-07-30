package com.boardgo.domain.user.repository;

import com.boardgo.domain.user.entity.UserEntity;
import com.boardgo.domain.user.repository.dto.UserDto;

public interface UserDslRepository {
    void save(UserEntity userEntity);
    UserDto selectAll();
}