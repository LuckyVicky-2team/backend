package com.boardgo.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.boardgo.domain.boardgame.repository.BoardGameGenreRepository;
import com.boardgo.domain.boardgame.repository.BoardGameRepository;
import com.boardgo.domain.boardgame.repository.GameGenreMatchRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BoardGameInitializer implements ApplicationRunner {
	private final BoardGameRepository boardGameRepository;
	private final BoardGameGenreRepository boardGameGenreRepository;
	private final GameGenreMatchRepository genreMatchRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception {

	}
	//
	// public List<BoardGameEntity> generateBoardGameData() {
	// 	List<BoardGameEntity> boardGameEntityList = new ArrayList<>();
	// 	for (int j = 0; j < 10; j++) {
	// 		boardGameGenreRepository.save(BoardGameGenreEntity.builder()
	// 			.genre("장르" + j)
	// 			.build());
	// 	}
	// 	for (int i = 0; i < 10; i++) {
	// 		BoardGameEntity entity = BoardGameEntity.builder()
	// 			.title("title" + i)
	// 			.minPeople(1 + i)
	// 			.maxPeople(3 + i)
	// 			.maxPlaytime(100 + i)
	// 			.minPlaytime(10 + i)
	// 			.thumbnail("thumbnail" + i)
	// 			.build();
	// 		boardGameEntityList.add(entity);
	// 		BoardGameEntity savedBoardGame = boardGameRepository.save(entity);
	//
	// 	}
	// }
}
