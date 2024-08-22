package com.boardgo.domain.home.service;

import com.boardgo.domain.home.controller.request.SituationRequest;
import com.boardgo.domain.home.controller.response.SituationResponse;
import java.util.List;

public interface HomeQueryUseCase {

    List<SituationResponse> getSituationBoardGame(SituationRequest situationRequest);
}
