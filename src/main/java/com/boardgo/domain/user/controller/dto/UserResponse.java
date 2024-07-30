package com.boardgo.domain.user.controller.dto;

import com.boardgo.domain.user.entity.UserEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserResponse {
    int age;
    String name;

    public static UserResponse toResponse(UserEntity userEntity) {
        return new UserResponse(userEntity.getAge(), userEntity.getName());
    }

    public UserResponse(int age, String name) {
        this.age = age;
        this.name = name;
    }
}
