package com.boardgo.domain.boardgame.repository.dto;

import com.querydsl.core.annotations.QueryProjection;

public record SituationBoardGameDto(
        String title, String thumbnail, int minPlaytime, int maxPlaytime, String genre) {
    @QueryProjection
    public SituationBoardGameDto {}
}
