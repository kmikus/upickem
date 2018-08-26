package com.upickem.repository;

import com.upickem.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRespository extends JpaRepository<Game, Long> {

    public Optional<Game> findByGameId(Long gameId);
}
