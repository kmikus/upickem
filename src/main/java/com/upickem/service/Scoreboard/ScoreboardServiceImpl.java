package com.upickem.service.Scoreboard;

import com.upickem.model.League;
import com.upickem.model.LeagueMember;
import com.upickem.model.Pick;
import com.upickem.repository.DTO.GetPointsOfMembersInLeagueByYearDto;
import com.upickem.repository.LeagueMemberRepository;
import com.upickem.repository.LeagueRepository;
import com.upickem.repository.PickRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.*;

@Service
public class ScoreboardServiceImpl implements ScoreboardService {

    @Autowired
    private PickRepository pickRepository;

    @Autowired
    private LeagueMemberRepository leagueMemberRepository;

    @Autowired
    private LeagueRepository leagueRepository;

    @Override
    public Map<String, Integer> getPointOfMembersInLeagueByYear(Long leagueId, Year year) {

        Optional<League> league = leagueRepository.findById(leagueId);
        if (!league.isPresent()) {
            return new HashMap<>();
        }

        List<Pick> picks = pickRepository.findAllByLeagueAndGameYear(league.get(), year);

        List<LeagueMember> leagueMembers = leagueMemberRepository.findAllByLeague(league.get());

        Map<String, Integer> usernameAndPoints = new HashMap<>();
        leagueMembers.forEach(leagueMember -> {
            usernameAndPoints.put(leagueMember.getUser().getUsername(), 0);
        });

        picks.forEach(pick -> {
            usernameAndPoints.keySet().forEach(username -> {
                if (username.equals(pick.getUser().getUsername()) && pick.getPointActual() != 0) {
                    usernameAndPoints.replace(username, usernameAndPoints.get(username)+1);
                }
            });
        });

        return usernameAndPoints;
    }

}
