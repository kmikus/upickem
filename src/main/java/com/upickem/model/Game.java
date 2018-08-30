package com.upickem.model;

import com.upickem.model.audit.TableAudit;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
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

    @Min(0)
    @Max(300)
    @NotNull
    private Long homeScore;

    @NotNull
    private Team awayTeam;

    @Min(0)
    @Max(300)
    @NotNull
    private Long awayScore;

    @NotNull
    private SeasonType seasonType;

    private Team winner;

    public Game(Long gameId, @NotNull Year year, @NotNull Long week, @NotNull LocalDateTime dateAndTime, @NotNull Team homeTeam, @NotNull Long homeScore, @NotNull Team awayTeam, @NotNull Long awayScore, @NotNull SeasonType seasonType, Team winner) {
        this.gameId = gameId;
        this.year = year;
        this.week = week;
        this.dateAndTime = dateAndTime;
        this.homeTeam = homeTeam;
        this.homeScore = homeScore;
        this.awayTeam = awayTeam;
        this.awayScore = awayScore;
        this.seasonType = seasonType;
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

    @NotNull
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

    @NotNull
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
}
