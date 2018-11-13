package com.upickem.service.Scoreboard;

import com.upickem.model.League;
import com.upickem.model.LeagueMember;
import com.upickem.model.Pick;
import com.upickem.repository.DTO.GetPointsOfMembersInLeagueByYearDto;
import com.upickem.repository.LeagueMemberRepository;
import com.upickem.repository.LeagueRepository;
import com.upickem.repository.PickRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.*;

@Service
public class ScoreboardServiceImpl implements ScoreboardService {

    private Logger logger = LoggerFactory.getLogger(ScoreboardServiceImpl.class);

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
            logger.warn("No league found for league id: " + leagueId);
            return new HashMap<>();
        }
        logger.info("Found league for league id: " + leagueId);

        List<Pick> picks = pickRepository.findAllByLeagueAndGameYear(league.get(), year);

        if (picks.isEmpty()) {
            logger.warn(String.format("No picks found for league id: %s and year: %s", leagueId.toString(), year.toString()));
            return new HashMap<>();
        }

        List<LeagueMember> leagueMembers = leagueMemberRepository.findAllByLeague(league.get());
        if (leagueMembers.isEmpty()) {
            logger.warn("No league members found for league id: " + leagueId);
            return new HashMap<>();
        }

        Map<String, Integer> usernameAndPoints = new HashMap<>();
        leagueMembers.forEach(leagueMember -> {
            usernameAndPoints.put(leagueMember.getUser().getUsername(), 0);
        });

        picks.forEach(pick -> {
            usernameAndPoints.keySet().forEach(username -> {
                logger.info(username);
                logger.info(pick.toString());
                if (username.equalsIgnoreCase(pick.getUser().getUsername()) && pick.getPointActual() != null && pick.getPointActual() != 0) {
                    usernameAndPoints.replace(username, usernameAndPoints.get(username) + 1);
                }
            });
        });

        return usernameAndPoints;
    }

}
