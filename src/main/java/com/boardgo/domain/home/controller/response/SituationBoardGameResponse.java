package com.boardgo.domain.home.controller.response;

import java.util.List;

public record SituationBoardGameResponse(
        String title, String thumbnail, int minPlaytime, int maxPlaytime, List<String> genres) {}
