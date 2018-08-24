package com.upickem.repository;

import com.upickem.model.League;
import com.upickem.model.LeagueUser;
import com.upickem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LeagueUserRepository extends JpaRepository<LeagueUser, Long> {

    public boolean existsByUserAndLeague(User user, League league);
}
