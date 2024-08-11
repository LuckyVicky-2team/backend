package com.boardgo.domain.boardgame.repository;

import com.boardgo.domain.boardgame.entity.BoardGameEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardGameRepository extends JpaRepository<BoardGameEntity, Long> {

    @Query("SELECT b.id FROM BoardGameEntity b WHERE b.id IN :idList")
    List<Long> findByIdIn(@Param("idList") List<Long> idList);
}