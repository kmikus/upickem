package com.upickem.controller;

import com.upickem.model.*;
import com.upickem.payload.ApiResponse;
import com.upickem.payload.PicksCreateRequest;
import com.upickem.repository.GameRespository;
import com.upickem.repository.LeagueRepository;
import com.upickem.repository.PickRepository;
import com.upickem.repository.UserRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/picks")
public class PickController {

    @Autowired
    PickRepository pickRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LeagueRepository leagueRepository;

    @Autowired
    GameRespository gameRespository;

    //Todo move this to a service

    // TODO add season type
    @RequestMapping
    public ResponseEntity<?> getPicksForLoggedInUserByLeague(@RequestParam Long leagueId,
                                                             @RequestParam Long year,
                                                             @RequestParam Long week) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).get();
        League league = leagueRepository.findById(leagueId).get();
        Year yearInput = Year.parse(year.toString());
        List<Pick> picks = pickRepository.findByUserAndLeagueAndGameYearAndGameWeek(user, league, yearInput, week);
        return ResponseEntity.ok(new ApiResponse<>(true, picks));
    }

    @PostMapping("/create")
    public ResponseEntity<?> insertPicks(@RequestBody PicksCreateRequest request) {
        List<Pick> picks = new ArrayList<>();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).get();
        League league = leagueRepository.findById(request.getLeagueId()).get();

        for (HashMap<String, String> gameIdAndWinner : request.getGameIdsAndWinners()) {

            // TODO prevent picks from being saved after game starts

            Long gameId = Long.parseLong(gameIdAndWinner.get("gameId"));
            Game game = gameRespository.findByGameId(gameId).get();
            Timestamp gameDateAndTime = game.getDateAndTime();
            if (gameDateAndTime.toLocalDateTime().isBefore(LocalDateTime.now())) {
                return ResponseEntity.ok(new ApiResponse<>(false, "Cannot save picks after game starts"));
            }
            Team winner = Team.valueOf(gameIdAndWinner.get("value"));

            Optional<Pick> pickFromDb = pickRepository.findByUserAndLeagueAndGame(user, league, game);
            if (pickFromDb.isPresent()) {
                Pick pick = pickFromDb.get();
                pick.setWinningTeam(winner);
                picks.add(pick);
            } else {
                Pick pick = new Pick();
                pick.setGame(game);
                pick.setLeague(league);
                pick.setUser(user);
                pick.setWinningTeam(winner);
                picks.add(pick);
            }
        }

        return ResponseEntity.ok(new ApiResponse<>(true, pickRepository.save(picks).size()
                + " records saved"));
    }
}
