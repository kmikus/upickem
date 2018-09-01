package com.upickem.model;

import com.upickem.model.audit.TableAudit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "pick",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"user", "league", "game"})
)
public class Pick extends TableAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pickId;

    @NotNull
    @ManyToOne
    @JoinColumn(name="user")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name="league")
    private League league;

    @NotNull
    private Team winningTeam;

    private Long pointPotential = 1L;

    private Long pointActual;

    @NotNull
    @ManyToOne
    @JoinColumn(name="game")
    private Game game;

    public Pick(@NotNull User user, @NotNull League league, @NotNull Team winningTeam, Long pointPotential, Long pointActual, @NotNull Game game) {
        this.user = user;
        this.league = league;
        this.winningTeam = winningTeam;
        this.pointPotential = pointPotential;
        this.pointActual = pointActual;
        this.game = game;
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
    public Game getGame() {
        return game;
    }

    public void setGame(@NotNull Game game) {
        this.game = game;
    }
}

