package com.upickem.model;

import com.upickem.model.audit.TableAudit;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "pick")
public class Pick extends TableAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private Team winningTeam;

    @Size(min = 1)
    @ColumnDefault("1")
    private Long points;

    private Long gameId;

    public Pick(Team winningTeam, Long points, Long gameId) {
        this.winningTeam = winningTeam;
        this.points = points;
        this.gameId = gameId;
    }

    public Pick() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getWinningTeam() {
        return winningTeam;
    }

    public void setWinningTeam(Team winningTeam) {
        this.winningTeam = winningTeam;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}
