package com.upickem.repository;

import com.upickem.model.Game;
import com.upickem.model.League;
import com.upickem.model.Pick;
import com.upickem.model.User;
import com.upickem.repository.DTO.GetPointsOfMembersInLeagueByYearDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Year;
import java.util.List;
import java.util.Optional;

public interface PickRepository extends JpaRepository<Pick, Long> {

    Optional<Pick> findByUserAndLeagueAndGame(User user, League league, Game game);

    List<Pick> findByUserAndLeagueAndGameYearAndGameWeek(User user, League league, Year year, Long week);

    @Query("select p from Pick p where p.pointActual is null and p.game.winner is not null")
    List<Pick> findPicksReadyToBeScored();

    List<Pick> findAllByLeagueAndGameYear(League league, Year year);
}
