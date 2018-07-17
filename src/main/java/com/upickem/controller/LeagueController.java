package com.upickem.controller;

import com.upickem.model.League;
import com.upickem.payload.LeagueRequest;
import com.upickem.repository.LeagueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Random;

@RestController
@RequestMapping("api/league")
public class LeagueController {

    Logger log = LoggerFactory.getLogger(LeagueController.class);

    @Autowired
    LeagueRepository leagueRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createLeague(@Valid @RequestBody LeagueRequest leagueRequest) {
        League league = new League();

        league.setName(leagueRequest.getName());
        String suffix = "";
        Random rand = new Random();
        for (int i=0; i<5; i++) {
            suffix += rand.nextInt(9);
        }
        league.setSuffix(suffix);

        log.info("random suffix for " + leagueRequest.getName() + " is " + suffix);

        leagueRepository.saveAndFlush(league);

        log.info("League created for " + leagueRequest.getName());

        return new ResponseEntity<>(true, HttpStatus.CREATED);

    }

}
