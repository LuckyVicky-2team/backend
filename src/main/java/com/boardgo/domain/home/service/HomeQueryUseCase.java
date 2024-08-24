package com.boardgo.domain.home.service;

import com.boardgo.domain.boardgame.entity.SituationType;
import com.boardgo.domain.home.controller.response.SituationBoardGameResponse;
import java.util.List;

public interface HomeQueryUseCase {

    List<SituationBoardGameResponse> getSituationBoardGame(SituationType situationType);

    //    List<CumulativePopularityResponse> getCumulativePopularity();
}
