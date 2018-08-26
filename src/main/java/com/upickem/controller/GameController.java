package com.upickem.controller;

import com.upickem.model.Game;
import com.upickem.payload.ApiResponse;
import com.upickem.payload.GameCreateRequest;
import com.upickem.repository.GameRespository;
import com.upickem.service.GameService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("api/game")
public class GameController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    GameService gameService;

    @Autowired
    GameRespository gameRespository;

    @GetMapping("/")
    public ResponseEntity<?> getAllGames() {
        return ResponseEntity.ok(new ApiResponse<>(true, gameRespository.findAll()));
    }

    @PostMapping("/saveGamesForYearAndWeek")
    public ResponseEntity<?> saveGamesForYearAndWeek(@RequestBody GameCreateRequest request) {
        List<Game> games = gameService.pullGameDataFromRemoteServer(Year.parse(request.getYear().toString()), request.getWeek());
        try {
            gameService.saveGames(games);
        } catch (DataIntegrityViolationException e) {
            log.error("Tried to save games that already exist", e);
            return ResponseEntity.ok(new ApiResponse<>(
                    false, "Tried to save games that already exist"));
        } catch (Exception e) {
            log.error("Unknown error occurred", e);
            return ResponseEntity.ok(new ApiResponse<>(false, "Unknown error occurred"));
        }
        return new ResponseEntity<>(new ApiResponse<>(
                true, games.size() + " game records created or updated"), HttpStatus.CREATED);
    }
}
