package com.boardgo.domain.home.service;

import com.boardgo.domain.boardgame.repository.BoardGameGenreRepository;
import com.boardgo.domain.boardgame.repository.BoardGameRepository;
import com.boardgo.domain.boardgame.repository.GameGenreMatchRepository;
import com.boardgo.domain.boardgame.repository.dto.SituationBoardGameDto;
import com.boardgo.domain.home.controller.request.SituationRequest;
import com.boardgo.domain.home.controller.response.SituationBoardGameResponse;
import com.boardgo.domain.home.enums.SituationType;
import com.boardgo.domain.mapper.HomeMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeQueryService implements HomeQueryUseCase {

    private final BoardGameRepository boardGameRepository;
    private final HomeMapper homeMapper;
    private final BoardGameGenreRepository boardGameGenreRepository;
    private final GameGenreMatchRepository gameGenreMatchRepository;

    @Override
    public List<SituationBoardGameResponse> getSituationBoardGame(
            SituationRequest situationRequest) {
        SituationType situationType = SituationType.valueOf(situationRequest.situationType());
        List<SituationBoardGameDto> situationBoardGames = null;
        switch (situationType) {
            case TWO -> {
                situationBoardGames = boardGameRepository.findByMaxPeopleBetween(2);
            }
            case THREE -> {
                situationBoardGames = boardGameRepository.findByMaxPeopleBetween(3);
            }
            case MANY -> {
                situationBoardGames = boardGameRepository.findByMaxPeopleBetween(4);
            }
            case ALL -> {
                situationBoardGames = boardGameRepository.findByMaxPeopleBetween(0);
            }
        }

        Map<String, SituationBoardGameResponse> boardGameMap = new HashMap<>();
        for (SituationBoardGameDto gameDto : situationBoardGames) {
            boardGameMap.merge(
                    gameDto.title(),
                    homeMapper.toSituationBoardGameResponse(gameDto),
                    (existing, replacement) -> {
                        existing.genres().addAll(replacement.genres());
                        return existing;
                    });
        }
        return new ArrayList<>(boardGameMap.values());
    }
}
