package com.upickem.model;

import com.upickem.model.audit.TableAudit;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "pick")
public class Pick extends TableAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pickId;

    @NotNull
    @ManyToOne
    private User user;

    @NotNull
    @ManyToOne
    private League league;

    @NotNull
    private Team winningTeam;

    @Size(min = 1)
    @ColumnDefault("1")
    private Long pointPotential;

    @ColumnDefault("0")
    private Long pointActual;

    @NotNull
    @ManyToOne
    private Game gameId;

    public Pick(@NotNull User user, @NotNull League league, @NotNull Team winningTeam, Long pointPotential, Long pointActual, @NotNull Game gameId) {
        this.user = user;
        this.league = league;
        this.winningTeam = winningTeam;
        this.pointPotential = pointPotential;
        this.pointActual = pointActual;
        this.gameId = gameId;
    }

    public Pick() {
    }

    public Long getPickId() {
        return pickId;
    }

    public void setPickId(Long pickId) {
        this.pickId = pickId;
    }

    @NotNull
    public User getUser() {
        return user;
    }

    public void setUser(@NotNull User user) {
        this.user = user;
    }

    @NotNull
    public League getLeague() {
        return league;
    }

    public void setLeague(@NotNull League league) {
        this.league = league;
    }

    @NotNull
    public Team getWinningTeam() {
        return winningTeam;
    }

    public void setWinningTeam(@NotNull Team winningTeam) {
        this.winningTeam = winningTeam;
    }

    public Long getPointPotential() {
        return pointPotential;
    }

    public void setPointPotential(Long pointPotential) {
        this.pointPotential = pointPotential;
    }

    public Long getPointActual() {
        return pointActual;
    }

    public void setPointActual(Long pointActual) {
        this.pointActual = pointActual;
    }

    @NotNull
    public Game getGameId() {
        return gameId;
    }

    public void setGameId(@NotNull Game gameId) {
        this.gameId = gameId;
    }
}

