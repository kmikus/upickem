package com.upickem.payload;

import java.util.HashMap;
import java.util.List;

public class PicksCreateRequest {

    private Long leagueId;

    private List<HashMap<String, String>> gameIdsAndWinners;

    public Long getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(Long leagueId) {
        this.leagueId = leagueId;
    }

    public List<HashMap<String, String>> getGameIdsAndWinners() {
        return gameIdsAndWinners;
    }

    public void setGameIdsAndWinners(List<HashMap<String, String>> gameIdsAndWinners) {
        this.gameIdsAndWinners = gameIdsAndWinners;
    }
}
