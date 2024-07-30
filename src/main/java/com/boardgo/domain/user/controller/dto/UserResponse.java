package com.boardgo.domain.user.controller.dto;

import com.boardgo.domain.user.service.dto.UserDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserResponse {
    int age;
    String name;

    public UserResponse(UserDto userDto) {
        this.age = userDto.getAge();
        this.name = userDto.getName();
    }
}
