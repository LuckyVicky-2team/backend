package com.boardgo.domain.user.repository;

import com.boardgo.domain.user.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

<<<<<<< HEAD
public interface UserRepository extends JpaRepository<UserInfoEntity, Long> {}
=======
import com.boardgo.domain.user.entity.UserInfoEntity;

public interface UserRepository extends JpaRepository<UserInfoEntity, Long> {

	UserInfoEntity findByEmail(String email);
}
>>>>>>> 75e59da (Feat: UserDetailsService, UserDetails 추가)
