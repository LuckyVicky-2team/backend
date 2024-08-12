package com.boardgo.domain.boardgame.controller.request;

import java.util.List;

public record BoardGameCreateRequest(
        String title,
        Integer minPeople,
        Integer maxPeople,
        Integer minPlaytime,
        Integer maxPlaytime,
        List<String> genres) {}
