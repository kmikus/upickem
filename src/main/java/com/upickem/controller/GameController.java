package com.upickem.controller;

import com.upickem.model.Game;
import com.upickem.model.SeasonType;
import com.upickem.payload.ApiResponse;
import com.upickem.payload.CurrentWeekResponse;
import com.upickem.payload.GameRequest;
import com.upickem.repository.GameRespository;
import com.upickem.schedule.ScheduleManager;
import com.upickem.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("api/games")
public class GameController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    GameService gameService;

    @Autowired
    GameRespository gameRespository;

    @RequestMapping
    public ResponseEntity<?> queryGamesWeekYearAndSeasonType(
            @RequestParam("week") Long week,
            @RequestParam(value="year") Long year,
            @RequestParam("seasonType") SeasonType seasonType) {
        List<Game> response;
        try {
            response = gameRespository.findGamesByYearAndWeekAndSeasonTypeOrderByDateAndTime(Year.of(year.intValue()), week, seasonType);
        } catch (Exception e) {
            String errorMessage = String.format("Could not fetch games for year: %s, week: %s, and season type: %s",
                    year, week, seasonType);
            log.error(errorMessage, e);
            return ResponseEntity.ok(new ApiResponse<>(false, errorMessage));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, response));

    }

    @GetMapping("/currentWeek")
    public ResponseEntity<?> getGamesForCurrentWeek() {
        return ResponseEntity.ok(new ApiResponse<>(true, new CurrentWeekResponse(ScheduleManager
                .getCurrentWeek(), gameService.getGamesForCurrentWeek(), ScheduleManager.getCurrentSeasonType())));
    }

    @PostMapping("/saveScheduleForYear/{seasonYear}")
    public ResponseEntity<?> saveScheduleForYear(@PathVariable Long seasonYear) {
        Long numRecordsSaved = gameService.saveScheduleForYear(Year.parse(seasonYear.toString()));
        if (numRecordsSaved > 0) {
            return ResponseEntity.ok(new ApiResponse<>(true, numRecordsSaved + " records saved"));
        } else {
            return ResponseEntity.ok(new ApiResponse<>(false, "Unable to save schedule"));
        }
    }

    @PostMapping("/datesForWeek")
    public ResponseEntity<?> getDatesForWeek(@RequestBody GameRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(true, gameService.getDatesOfGamesForWeekFromRemote(
                Year.parse(request.getCalendarYear().toString()), request.getNflWeek(), request.getSeasonType()
        )));
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllGames() {
        return ResponseEntity.ok(new ApiResponse<>(true, gameRespository.findAll()));
    }

    @PostMapping("/saveGamesForYearAndWeek")
    public ResponseEntity<?> saveGamesForYearAndWeek(@RequestBody GameRequest request) {
        List<Game> games = gameService.pullGameDataFromRemoteServer(Year.parse(request.getCalendarYear().toString()),
                request.getNflWeek(), request.getSeasonType());
        try {
            gameService.saveGames(games);
        } catch (DataIntegrityViolationException e) {
            log.error("Tried to save games that already exist", e);
            return ResponseEntity.ok(new ApiResponse<>(
                    false, "Tried to save games that already exist"));
        } catch (Exception e) {
            log.error("Unknown error occurred", e);
            return ResponseEntity.ok(new ApiResponse<>(false, "Unknown error occurred" +
                    "while trying to save games for year:" + request.getCalendarYear() + " and week: " +
                    request.getNflWeek()));
        }
        return new ResponseEntity<>(new ApiResponse<>(
                true, games.size() + " game records created or updated"), HttpStatus.CREATED);
    }
}
