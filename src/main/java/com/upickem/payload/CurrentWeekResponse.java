package com.upickem.payload;

import com.upickem.model.Game;
import com.upickem.model.SeasonType;

import java.util.List;

public class CurrentWeekResponse {

    private Long week;

    private SeasonType seasonType;

    private List<Game> games;

    public CurrentWeekResponse(Long week, List<Game> games, SeasonType seasonType) {
        this.week = week;
        this.games = games;
        this.seasonType = seasonType;
    }

    public CurrentWeekResponse(Long week) {
        this.week = week;
    }

    public Long getWeek() {
        return week;
    }

    public void setWeek(Long week) {
        this.week = week;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public SeasonType getSeasonType() {
        return seasonType;
    }

    public void setSeasonType(SeasonType seasonType) {
        this.seasonType = seasonType;
    }
}
