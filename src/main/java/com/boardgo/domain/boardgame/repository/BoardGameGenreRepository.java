package com.boardgo.domain.boardgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boardgo.domain.boardgame.entity.BoardGameGenre;

public interface BoardGameGenreRepository extends JpaRepository<BoardGameGenre, Long> {
}
