package com.upickem.model;

import com.upickem.model.audit.TableAudit;

import javax.persistence.*;

@Entity
@Table(name="league_user_join")
public class LeagueUser extends TableAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long leagueUserId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="league_id")
    private League league;

    public LeagueUser(User user, League league) {
        this.user = user;
        this.league = league;
    }

    public LeagueUser() {
    }

    public Long getLeagueUserId() {
        return leagueUserId;
    }

    public void setLeagueUserId(Long leagueUserId) {
        this.leagueUserId = leagueUserId;
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
}
