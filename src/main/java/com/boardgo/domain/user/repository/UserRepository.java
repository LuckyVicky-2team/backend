package com.boardgo.domain.user.repository;


import com.boardgo.domain.user.entity.UserEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    void save(UserEntity userEntity);
    List<UserEntity> selectAll();
}
