package com.upickem.controller;

import com.upickem.exception.BadRequestException;
import com.upickem.model.LeagueMember;
import com.upickem.model.User;
import com.upickem.payload.ApiResponse;
import com.upickem.payload.UserLeaguesResponse;
import com.upickem.payload.UserTrimmedResponse;
import com.upickem.repository.LeagueMemberRepository;
import com.upickem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepository userRepository;

    @Autowired
    LeagueMemberRepository leagueMemberRepository;

    @GetMapping("/getLeaguesForLoggedInUser")
    public ResponseEntity<?> getLeaguesForLoggedInUser() {
        Optional<User> user = userRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());

        if (!user.isPresent()) {
            throw new BadRequestException("User not logged in");
        }

        List<LeagueMember> leagueMembers = leagueMemberRepository.findAllByUser(user.get());
        List<UserLeaguesResponse> leagues = new ArrayList<>();

        if (leagueMembers.size() > 0) {
            leagueMembers.forEach(leagueMember -> leagues.add(
                    new UserLeaguesResponse(leagueMember.getLeague().getId(),
                            leagueMember.getLeague().getName(),
                            leagueMember.getLeague().getSuffix()))
            );
        }

        log.info("Found " + leagueMembers.size() + " leagues for user: " + user.get().getUsername());

        return ResponseEntity.ok(new ApiResponse<>(true, leagues));
    }

    @PostMapping("/getUserByEmailOrUsername")
    public ResponseEntity<?> getUser(@RequestBody String emailOrUsername) {

        Optional<User> user = userRepository.findByUsernameOrEmail(emailOrUsername, emailOrUsername);
        if (!user.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(false, "No user found"));
        } else {
            UserTrimmedResponse response = new UserTrimmedResponse(user.get().getId(),
                    user.get().getFirstName(), user.get().getLastName(), user.get().getUsername());
            return ResponseEntity.ok(new ApiResponse<>(true, response));
        }
    }

}
