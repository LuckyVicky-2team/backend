package com.boardgo.domain.mapper;

import com.boardgo.domain.boardgame.controller.request.BoardGameCreateRequest;
import com.boardgo.domain.boardgame.entity.BoardGameEntity;
import com.boardgo.domain.boardgame.repository.projection.BoardGameSearchProjection;
import com.boardgo.domain.boardgame.repository.response.BoardGameSearchResponse;
import com.boardgo.domain.boardgame.repository.response.GenreSearchResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BoardGameMapper {
    BoardGameMapper INSTANCE = Mappers.getMapper(BoardGameMapper.class);

    @Mapping(source = "thumbnail", target = "thumbnail")
    BoardGameEntity toBoardGameEntity(
            BoardGameCreateRequest boardGameCreateRequest, String thumbnail);

    default BoardGameSearchResponse toBoardGameSearchResponse(
            BoardGameSearchProjection boardGameSearchProjection,
            List<GenreSearchResponse> genreSearchResponseList) {
        BoardGameGenreMapper boardGameGenreMapper = BoardGameGenreMapper.INSTANCE;
        return new BoardGameSearchResponse(
                boardGameSearchProjection.id(),
                boardGameSearchProjection.title(),
                boardGameSearchProjection.thumbnail(),
                genreSearchResponseList);
    }
}
