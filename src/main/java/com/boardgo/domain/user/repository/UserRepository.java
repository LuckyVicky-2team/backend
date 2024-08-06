package com.boardgo.domain.user.repository;

import com.boardgo.domain.user.entity.UserInfoEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserInfoEntity, Long> {

    Optional<UserInfoEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickName(String nickName);
}
