package com.upickem.repository;

import com.upickem.model.League;
import com.upickem.model.LeagueMember;
import com.upickem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeagueMemberRepository extends JpaRepository<LeagueMember, Long> {

    boolean existsByUserAndLeague(User user, League league);

    List<LeagueMember> findAllByUser(User user);

    List<LeagueMember> findAllByLeague(League league);
}
