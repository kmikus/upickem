package com.upickem.repository;

import com.upickem.model.Game;
import com.upickem.model.League;
import com.upickem.model.Pick;
import com.upickem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Year;
import java.util.List;
import java.util.Optional;

public interface PickRepository extends JpaRepository<Pick, Long> {

    public Optional<Pick> findByUserAndLeagueAndGame(User user, League league, Game game);

    public List<Pick> findByUserAndLeagueAndGameYearAndGameWeek(User user, League league, Year year, Long week);

    @Query("select p from Pick p where p.pointActual is null and p.game.winner is not null")
    public List<Pick> findPicksReadyToBeScored();
}
