package com.boardgo.domain.boardgame.repository.projection;

public record BoardGameByMeetingIdProjection(
        Long boardGameId, String title, String thumbnail, String genres) {}
