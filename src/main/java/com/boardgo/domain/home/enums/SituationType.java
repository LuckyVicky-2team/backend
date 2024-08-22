package com.boardgo.domain.home.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SituationType {
    TWO("2인 게임"),
    THREE("3인 게임"),
    MANY("다인원"),
    ALL("전체 장르");

    private String name;
}
