package com.upickem.repository;

import com.upickem.model.Game;
import com.upickem.model.League;
import com.upickem.model.Pick;
import com.upickem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PickRepository extends JpaRepository<Pick, Long> {

    public Optional<Pick> findByUserAndLeagueAndGame(User user, League league, Game game);
}
