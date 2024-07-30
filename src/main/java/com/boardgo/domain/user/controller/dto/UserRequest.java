package com.boardgo.domain.user.controller.dto;

import com.boardgo.domain.user.entity.UserEntity;

public class UserRequest {
    int age;
    String name;

    //todo. 만약 entity에 부분 저장/수정할 비즈니스 요청이 있울 경우 entity에 생성자가 많아 지는것 아닌가?
    public UserEntity toEntity(){
        return UserEntity.builder()
                .age(age)
                .name(name)
                .build();
    }
}
