package com.upickem.model;

import com.upickem.model.audit.TableAudit;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.Year;

@Entity
@Table(
        uniqueConstraints=
        @UniqueConstraint(columnNames={"year", "week", "homeTeam", "seasonType"})
)
public class Game extends TableAudit {

    @Id
    private Long gameId;

    @NotNull
    private Year year;

    @NotNull
    @Min(1)
    @Max(22)
    private Long week;

    @NotNull
    private LocalDateTime dateAndTime;

    @NotNull
    private Team homeTeam;

    private Long homeScore;

    @NotNull
    private Team awayTeam;

    private Long awayScore;

    @NotNull
    private SeasonType seasonType;

    private Quarter quarter;

    private Team winner;

    public Game(Long gameId, @NotNull Year year, @NotNull Long week, @NotNull LocalDateTime dateAndTime, @NotNull Team homeTeam, Long homeScore, @NotNull Team awayTeam, Long awayScore, @NotNull SeasonType seasonType, Quarter quarter, Team winner) {
        this.gameId = gameId;
        this.year = year;
        this.week = week;
        this.dateAndTime = dateAndTime;
        this.homeTeam = homeTeam;
        this.homeScore = homeScore;
        this.awayTeam = awayTeam;
        this.awayScore = awayScore;
        this.seasonType = seasonType;
        this.quarter = quarter;
        this.winner = winner;
    }

    public Game() {
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    @NotNull
    public Year getYear() {
        return year;
    }

    public void setYear(@NotNull Year year) {
        this.year = year;
    }

    @NotNull
    public Long getWeek() {
        return week;
    }

    public void setWeek(@NotNull Long week) {
        this.week = week;
    }

    @NotNull
    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(@NotNull Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Long getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(@NotNull Long homeScore) {
        this.homeScore = homeScore;
    }

    @NotNull
    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(@NotNull Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public Long getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(@NotNull Long awayScore) {
        this.awayScore = awayScore;
    }

    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    @NotNull
    public SeasonType getSeasonType() {
        return seasonType;
    }

    public void setSeasonType(@NotNull SeasonType seasonType) {
        this.seasonType = seasonType;
    }

    @NotNull
    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(@NotNull LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public Quarter getQuarter() {
        return quarter;
    }

    public void setQuarter(Quarter quarter) {
        this.quarter = quarter;
    }
}
