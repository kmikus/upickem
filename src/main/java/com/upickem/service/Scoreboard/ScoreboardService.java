package com.upickem.service.Scoreboard;

import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.Map;

@Service
public interface ScoreboardService {

    Map<String, Integer> getPointOfMembersInLeagueByYear(Long leagueId, Year year);
}
