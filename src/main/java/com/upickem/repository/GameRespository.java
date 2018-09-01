package com.upickem.repository;

import com.upickem.model.Game;
import com.upickem.model.SeasonType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Year;
import java.util.List;
import java.util.Optional;

public interface GameRespository extends JpaRepository<Game, Long> {

    public Optional<Game> findByGameId(Long gameId);

    public List<Game> findGamesByYear(Year year);

    public List<Game> findGamesByYearAndWeekAndSeasonTypeOrderByDateAndTime(
            Year year, Long week, SeasonType seasonType);
}
