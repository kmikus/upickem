package com.upickem.repository;

import com.upickem.model.UserLeaguePickJoin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLeaguePickRepository extends JpaRepository<UserLeaguePickJoin, Long> {
}
