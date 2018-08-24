package com.upickem.payload;

public class UserLeaguesResponse {

    private Long leagueId;

    private String leagueName;

    private String suffix;

    public UserLeaguesResponse(Long leagueId, String leagueName, String suffix) {
        this.leagueId = leagueId;
        this.leagueName = leagueName;
        this.suffix = suffix;
    }

    public UserLeaguesResponse() {
    }

    public Long getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(Long leagueId) {
        this.leagueId = leagueId;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
