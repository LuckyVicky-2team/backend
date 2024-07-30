package com.boardgo.domain.user.controller;

import com.boardgo.domain.user.controller.dto.UserRequest;
import com.boardgo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    void save(UserRequest userRequest){
        userService.save(userRequest.toEntity());
    }

}
