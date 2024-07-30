package com.boardgo.domain.user.repository;


import com.boardgo.domain.user.entity.UserEntity;

public interface UserRepository {
    void save(UserEntity userEntity);
    UserEntity selectAll();
}
