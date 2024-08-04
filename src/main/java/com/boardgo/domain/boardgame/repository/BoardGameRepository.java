package com.boardgo.domain.boardgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boardgo.domain.boardgame.entity.BoardGame;

public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {
}
