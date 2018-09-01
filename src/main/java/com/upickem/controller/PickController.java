package com.upickem.controller;

import com.upickem.model.Game;
import com.upickem.model.League;
import com.upickem.model.Pick;
import com.upickem.model.User;
import com.upickem.payload.ApiResponse;
import com.upickem.payload.PicksCreateRequest;
import com.upickem.repository.GameRespository;
import com.upickem.repository.LeagueRepository;
import com.upickem.repository.PickRepository;
import com.upickem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

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

//    @PostMapping("/create")
//    public ResponseEntity<?> insertPicks(@RequestBody PicksCreateRequest request) {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepository.findByUsername(username).get();
//        League league = leagueRepository.findById(request.getLeaugeId()).get();
//
//        return ResponseEntity.ok(new ApiResponse<>(true, pickRepository.save(picks)));
//    }
}
