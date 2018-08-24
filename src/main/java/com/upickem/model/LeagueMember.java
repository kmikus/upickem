package com.upickem.model;

import com.upickem.model.audit.TableAudit;

import javax.persistence.*;

@Entity
@Table(name="league_user_join")
public class LeagueMember extends TableAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long leagueUserId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="league_id")
    private League league;

    public LeagueMember(User user, League league) {
        this.user = user;
        this.league = league;
    }

    public LeagueMember() {
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
