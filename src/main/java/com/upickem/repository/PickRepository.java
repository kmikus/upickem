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

    public Optional<Pick> findByUserAndLeagueAndGame(User user, League league, Game game);

    public List<Pick> findByUserAndLeagueAndGameYearAndGameWeek(User user, League league, Year year, Long week);

    @Query("select p from Pick p where p.pointActual is null and p.game.winner is not null")
    public List<Pick> findPicksReadyToBeScored();

    @Query("select new com.upickem.repository.DTO.GetPointsOfMembersInLeagueByYearDto(lm.user, sum(p.pointActual))" +
            " from Pick p left outer join LeagueMember lm on lm.user = p.user where lm.league = :league and p.game.year = :year"+
            " group by p.user")
    public List<GetPointsOfMembersInLeagueByYearDto> getPointsOfMembersInLeagueByYear(
            @Param("league") League league, @Param("year") Year year);
}
