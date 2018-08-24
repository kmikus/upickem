package com.upickem.controller;

import com.upickem.model.League;
import com.upickem.model.LeagueMember;
import com.upickem.model.User;
import com.upickem.payload.UserLeaguesResponse;
import com.upickem.repository.LeagueMemberRepository;
import com.upickem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/user")
public class UserController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepository userRepository;

    @Autowired
    LeagueMemberRepository leagueMemberRepository;

    @GetMapping("/getLeagues")
    public ResponseEntity<?> getLeagues() {
        Optional<User> user = userRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());

        List<LeagueMember> leagueMembers = leagueMemberRepository.findAllByUser(user.get());
        List<UserLeaguesResponse> leagues = new ArrayList<>();

        leagueMembers.forEach(leagueMember -> leagues.add(
                new UserLeaguesResponse(leagueMember.getLeague().getId(),
                        leagueMember.getLeague().getName(),
                        leagueMember.getLeague().getSuffix()))
        );

        return ResponseEntity.ok(leagues);
    }

}
