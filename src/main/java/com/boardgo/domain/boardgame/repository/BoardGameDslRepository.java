package com.boardgo.domain.boardgame.repository;

import com.boardgo.domain.boardgame.controller.request.BoardGameSearchRequest;
import com.boardgo.domain.boardgame.repository.response.BoardGameSearchResponse;
import org.springframework.data.domain.Page;

public interface BoardGameDslRepository {

    Page<BoardGameSearchResponse> findBySearchWord(BoardGameSearchRequest boardGameSearchRequest);
}
