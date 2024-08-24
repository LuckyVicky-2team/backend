package com.boardgo.domain.boardgame.service.response;

import lombok.Getter;

@Getter
public class CumulativePopularityResponse {
    private Long boardGameId;
    private String title;
    private String thumbnail;
    private Long cumulativeCount;

    public CumulativePopularityResponse(Long boardGameId, String title, String thumbnail) {
        this.boardGameId = boardGameId;
        this.title = title;
        this.thumbnail = thumbnail;
    }

    public void updateCumulativeCount(Long cumulativeCount) {
        this.cumulativeCount = cumulativeCount;
    }
}
