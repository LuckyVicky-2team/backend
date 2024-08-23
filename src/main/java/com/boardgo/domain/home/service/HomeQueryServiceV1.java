package com.boardgo.domain.home.service;

import com.boardgo.domain.boardgame.repository.BoardGameRepository;
import com.boardgo.domain.boardgame.repository.dto.SituationBoardGameDto;
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
public class HomeQueryServiceV1 implements HomeQueryUseCase {

    private final BoardGameRepository boardGameRepository;
    private final HomeMapper homeMapper;

    @Override
    public List<SituationBoardGameResponse> getSituationBoardGame(String situationRequest) {
        SituationType situationType = SituationType.valueOf(situationRequest);
        List<SituationBoardGameDto> situationBoardGames =
                boardGameRepository.findByMaxPeopleBetween(situationType.getPeople());

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
