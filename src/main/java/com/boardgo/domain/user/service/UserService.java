package com.boardgo.domain.user.service;

import com.boardgo.domain.user.entity.UserEntity;
import com.boardgo.domain.user.repository.UserDslRepository;
import com.boardgo.domain.user.repository.UserRepository;
import com.boardgo.domain.user.repository.dto.UserDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {
    private final UserRepository userRepository;
    private final UserDslRepository userDslRepository;

    public void save(UserEntity userEntity){
        userRepository.save(userEntity);
    }

    public List<UserEntity> selectAll(){
        return userRepository.selectAll();
    }

    public void saveDsl(UserEntity userEntity){
        userDslRepository.save(userEntity);
    }

    public UserEntity selectDsl(){
        UserDto userDto = userDslRepository.selectAll();
        // UserDto > UserEntity
        return userDto.toEntity();
    }
}
