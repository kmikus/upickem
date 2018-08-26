package com.upickem.repository;

import com.upickem.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRespository extends JpaRepository<Game, Long> {
}
