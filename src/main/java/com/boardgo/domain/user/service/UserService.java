package com.boardgo.domain.user.service;

import com.boardgo.domain.user.controller.dto.UserResponse;
import com.boardgo.domain.user.entity.UserEntity;
import com.boardgo.domain.user.repository.UserDslRepository;
import com.boardgo.domain.user.repository.UserRepository;
import com.boardgo.domain.user.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserDslRepository userDslRepository;

    public void save(UserEntity userEntity){
        userRepository.save(userEntity);
    }

    public UserEntity selectAll(){
        return userRepository.selectAll();
    }

    public void saveDsl(UserEntity userEntity){
        userDslRepository.save(userEntity);
    }

    public UserResponse selectAllDsl(){
        UserDto userDto = userDslRepository.selectAll();
        return new UserResponse(userDto);
    }
}
