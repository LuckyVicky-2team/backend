package com.boardgo.domain.user.repository;

import com.boardgo.domain.user.entity.UserEntity;
import com.boardgo.domain.user.service.dto.UserDto;

public class UserDslRepository {

    public void save(UserEntity userEntity) {}

    public UserDto selectAll() {
        return new UserDto();
    }
}
