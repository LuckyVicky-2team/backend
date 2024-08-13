package com.boardgo.domain.mapper;

import com.boardgo.domain.boardgame.controller.request.BoardGameCreateRequest;
import com.boardgo.domain.boardgame.entity.BoardGameEntity;
import com.boardgo.domain.boardgame.repository.projection.BoardGameSearchProjection;
import com.boardgo.domain.boardgame.repository.response.BoardGameSearchResponse;
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
            BoardGameSearchProjection boardGameSearchProjection) {
        return new BoardGameSearchResponse(
                boardGameSearchProjection.id(),
                boardGameSearchProjection.title(),
                boardGameSearchProjection.thumbnail(),
                List.of(boardGameSearchProjection.genres().split(",")));
    }
}
