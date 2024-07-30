package com.boardgo.domain.user.repository.dto;

import com.boardgo.domain.user.entity.UserEntity;
import lombok.Data;

@Data
public class UserDto {
    int age;
    String name;

    public UserEntity toEntity(){
        return UserEntity.builder()
                .age(age)
                .name(name)
                .build();
    }
}
