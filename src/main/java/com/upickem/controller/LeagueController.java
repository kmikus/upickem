package com.upickem.controller;

import com.upickem.model.League;
import com.upickem.model.LeagueMember;
import com.upickem.model.User;
import com.upickem.payload.ApiResponse;
import com.upickem.payload.LeagueRequest;
import com.upickem.repository.LeagueMemberRepository;
import com.upickem.repository.LeagueRepository;
import com.upickem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("api/leagues")
public class LeagueController {

    Logger log = LoggerFactory.getLogger(LeagueController.class);

    @Value("${league.maxMembers}")
    private Long LEAGUE_MAX_MEMBERS;

    @Autowired
    LeagueRepository leagueRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LeagueMemberRepository leagueMemberRepository;

    @GetMapping("/")
    public ResponseEntity<?> getAllLeagues() {
        return ResponseEntity.ok(new ApiResponse<>(true, leagueRepository.findAll()));
    }

    @GetMapping("/getMaxMembers")
    public ResponseEntity<?> getMaxMembers() {
        return ResponseEntity.ok(new ApiResponse<>(true, LEAGUE_MAX_MEMBERS));
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

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(username);
        league.setCommissioner(user.get());

        leagueRepository.saveAndFlush(league);
        leagueMemberRepository.saveAndFlush(new LeagueMember(user.get(), league));

        if (leagueRequest.getUsernamesOrEmails().size() > LEAGUE_MAX_MEMBERS) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Too many members submitted"));
        }

        for (String usernameOrEmail : leagueRequest.getUsernamesOrEmails()) {
            leagueMemberRepository.saveAndFlush(new LeagueMember(
                    userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).get(), league));
        }

        log.info("League created for " + leagueRequest.getName());
        log.info("random suffix for " + leagueRequest.getName() + " is " + suffix);

        return new ResponseEntity<>(new ApiResponse<>(true, "League created"), HttpStatus.CREATED);

    }

    // TODO only authorize the owner of the league
    // TODO change to username or email
    @PostMapping("/{leagueId}/addUser/{userId}")
    public ResponseEntity<?> addUserToLeague(@PathVariable("leagueId") Long leagueId,
                                             @PathVariable("userId") Long userId) {

        String responseMessage;

        // Can't find user
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) {
            responseMessage = "User not found for user id: " + userId;
            log.info(responseMessage);
            return ResponseEntity.ok(new ApiResponse<>(false, responseMessage));
        }

        // Can't find league
        Optional<League> league = leagueRepository.findById(leagueId);
        if(!league.isPresent()) {
            responseMessage = "League not found for league id: " + leagueId;
            log.info(responseMessage);
            return ResponseEntity.ok(new ApiResponse<>(false, responseMessage));        }

        // User is already member of league
        if(leagueMemberRepository.existsByUserAndLeague(user.get(), league.get())) {
            responseMessage = "User: " + user.get().getUsername() + " is already a member of league id: " +
                    league.get().getId();
            log.info(responseMessage);
            return ResponseEntity.ok(new ApiResponse<>(false, responseMessage));        }

        // Success
        LeagueMember leagueMember = new LeagueMember(user.get(), league.get());
        leagueMemberRepository.saveAndFlush(leagueMember);
        responseMessage = "User: " + user.get().getUsername() + " added to league id: " +
                league.get().getId();
        log.info(responseMessage);
        return ResponseEntity.ok(new ApiResponse<>(true, responseMessage));    }

}
