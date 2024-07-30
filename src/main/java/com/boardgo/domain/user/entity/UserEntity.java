package com.boardgo.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {
    @Id
    @GeneratedValue
    private Long id;

    private int age;

    private String name;

    @Builder
    public UserEntity(int age, String name) {
        this.age = age;
        this.name = name;
    }
}
