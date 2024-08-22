package com.boardgo.domain.home.service;

import com.boardgo.domain.boardgame.repository.BoardGameGenreRepository;
import com.boardgo.domain.boardgame.repository.BoardGameRepository;
import com.boardgo.domain.boardgame.repository.GameGenreMatchRepository;
import com.boardgo.domain.home.controller.request.SituationRequest;
import com.boardgo.domain.home.controller.response.SituationResponse;
import com.boardgo.domain.home.enums.SituationType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeQueryService implements HomeQueryUseCase {

    private final BoardGameRepository boardGameRepository;
    private final BoardGameGenreRepository boardGameGenreRepository;
    private final GameGenreMatchRepository gameGenreMatchRepository;

    @Override
    public List<SituationResponse> getSituationBoardGame(SituationRequest situationRequest) {
        SituationType situationType = SituationType.valueOf(situationRequest.situationType());
        switch (situationType) {
            case TWO -> {
                // TODO.
            }
            case THREE -> {}
            case MANY -> {}
            case ALL -> {}
        }

        return null;
    }
}
