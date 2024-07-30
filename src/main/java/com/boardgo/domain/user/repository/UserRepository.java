package com.boardgo.domain.user.repository;


import com.boardgo.domain.user.entity.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    void save(UserEntity userEntity);
    UserEntity selectAll();
}
