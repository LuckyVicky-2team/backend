package com.boardgo.domain.user.controller;

import com.boardgo.domain.user.controller.dto.UserRequest;
import com.boardgo.domain.user.controller.dto.UserResponse;
import com.boardgo.domain.user.service.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserUseCase userUseCase;

    void save(UserRequest userRequest){
        userUseCase.save(userRequest.toEntity());
    }

    void selectAllDsl(){
        UserResponse userResponse = UserResponse.toResponse(userUseCase.selectDsl());
    }

}
