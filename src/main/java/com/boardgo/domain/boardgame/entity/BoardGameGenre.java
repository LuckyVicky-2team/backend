package com.boardgo.domain.boardgame.entity;

import com.boardgo.common.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "board_game_genre_id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardGameGenre extends BaseEntity {
	@Id
	@Column(name = "board_game_genre_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String genre;
}
