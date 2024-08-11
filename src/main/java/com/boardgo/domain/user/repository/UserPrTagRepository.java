package com.boardgo.domain.user.repository;

import com.boardgo.domain.user.entity.UserPrTagEntity;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface UserPrTagRepository
        extends JpaRepository<UserPrTagEntity, Long>, UserPrTagJdbcRepository {

    List<UserPrTagEntity> findByUserInfoId(Long userInfoId);
}
