package com.upickem.controller;

import com.upickem.model.League;
import com.upickem.model.LeagueUser;
import com.upickem.model.User;
import com.upickem.payload.LeagueRequest;
import com.upickem.repository.LeagueRepository;
import com.upickem.repository.LeagueUserRepository;
import com.upickem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("api/league")
public class LeagueController {

    Logger log = LoggerFactory.getLogger(LeagueController.class);

    @Autowired
    LeagueRepository leagueRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LeagueUserRepository leagueUserRepository;

    @GetMapping("/")
    public ResponseEntity<?> getAllLeagues() {
        return new ResponseEntity<>(leagueRepository.findAll(), HttpStatus.OK);
    }

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

    @PostMapping("/{leagueId}/addUser/{userId}")
    public ResponseEntity<?> addUserToLeague(@PathVariable("leagueId") Long leagueId,
                                             @PathVariable("userId") Long userId) {

        String responseMessage;

        // Can't find user
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) {
            responseMessage = "User not found for user id: " + userId;
            log.info(responseMessage);
            return new ResponseEntity<>(Collections.singletonMap("error", responseMessage), HttpStatus.OK);
        }

        // Can't find league
        Optional<League> league = leagueRepository.findById(leagueId);
        if(!league.isPresent()) {
            responseMessage = "League not found for league id: " + leagueId;
            log.info(responseMessage);
            return new ResponseEntity<>(Collections.singletonMap("error", responseMessage), HttpStatus.OK);
        }

        // User is already member of league
        if(leagueUserRepository.existsByUserAndLeague(user.get(), league.get())) {
            responseMessage = "User: " + user.get().getUsername() + " is already a member of league id: " +
                    league.get().getId();
            log.info(responseMessage);
            return new ResponseEntity<>(Collections.singletonMap("error", responseMessage), HttpStatus.OK);
        }

        // Success
        LeagueUser leagueUser = new LeagueUser(user.get(), league.get());
        leagueUserRepository.saveAndFlush(leagueUser);
        responseMessage = "User: " + user.get().getUsername() + " added to league id: " +
                league.get().getId();
        log.info(responseMessage);
        return new ResponseEntity<>(Collections.singletonMap("success", true), HttpStatus.OK);
    }

}
