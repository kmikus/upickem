package com.upickem.service;

import com.upickem.model.Game;
import com.upickem.model.SeasonType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@Service
public interface GameService {

    public List<Game> pullGameDataFromRemoteServer(Year year, Long week, SeasonType seasonType);

    public List<Game> saveGames(List<Game> games);

    public List<LocalDate> getDatesOfGamesForWeekFromRemote(Year year, Long week, SeasonType seasonType);

    public Long saveScheduleForYear(Year year);

    public List<Game> findGamesByYear(Year year);

    public List<Game> getGamesForCurrentWeek();
}
