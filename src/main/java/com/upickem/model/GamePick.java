package com.upickem.model;

import com.upickem.model.audit.TableAudit;

import javax.persistence.*;

@Entity
@Table(name="user_league_pick_join")
public class GamePick extends TableAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long gamePickId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="league_id")
    private League league;

    @ManyToOne
    @JoinColumn(name="pick_id")
    private Pick pick;

    public GamePick(User user, League league, Pick pick) {
        this.user = user;
        this.league = league;
        this.pick = pick;
    }

    public GamePick() {
    }

    public Long getGamePickId() {
        return gamePickId;
    }

    public void setGamePickId(Long gamePickId) {
        this.gamePickId = gamePickId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Pick getPick() {
        return pick;
    }

    public void setPick(Pick pick) {
        this.pick = pick;
    }
}
