package com.upickem.controller;

import com.upickem.repository.GameRespository;
import com.upickem.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Year;

@RestController
@RequestMapping("api/game")
public class GameController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    GameRespository gameRespository;

    @Autowired
    GameService gameService;

    // TODO this
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(gameService.pullGameDataFromRemoteServer(Year.parse("2017"), 1L));
    }
}
