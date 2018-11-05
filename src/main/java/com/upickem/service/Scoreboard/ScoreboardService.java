package com.upickem.service.Scoreboard;

import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.Map;

@Service
public interface ScoreboardService {

    //TODO make controller for this
    Map<String, Integer> getPointOfMembersInLeagueByYear(Long leagueId, Year year);
}
