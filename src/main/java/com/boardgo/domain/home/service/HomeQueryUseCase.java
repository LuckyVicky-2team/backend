package com.boardgo.domain.home.service;

import com.boardgo.domain.home.controller.request.SituationRequest;
import com.boardgo.domain.home.controller.response.SituationBoardGameResponse;
import java.util.List;

public interface HomeQueryUseCase {

    List<SituationBoardGameResponse> getSituationBoardGame(SituationRequest situationRequest);
}
