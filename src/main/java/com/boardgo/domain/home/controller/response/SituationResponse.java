package com.boardgo.domain.home.controller.response;

import java.util.List;

public record SituationResponse(
        String title, String thumbnail, int minPlaytime, int maxPlaytime, List<String> genres) {}
