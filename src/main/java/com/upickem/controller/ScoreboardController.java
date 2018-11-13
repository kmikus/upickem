package com.upickem.controller;

import com.upickem.service.Scoreboard.ScoreboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.Map;

@RestController
@RequestMapping("api/scoreboard")
public class ScoreboardController {

    @Autowired
    ScoreboardService scoreboardService;

    @GetMapping("/q")
    @ResponseBody
    public Map<String, Integer> getPointOfMembersInLeagueByYear(@RequestParam Long leagueId, @RequestParam Long year) {
        return scoreboardService.getPointOfMembersInLeagueByYear(leagueId, Year.of(year.intValue()));
    }
}
