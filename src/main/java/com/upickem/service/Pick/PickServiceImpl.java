package com.upickem.service.Pick;

import com.upickem.model.League;
import com.upickem.model.Pick;
import com.upickem.repository.DTO.GetPointsOfMembersInLeagueByYearDto;
import com.upickem.repository.PickRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class PickServiceImpl implements PickService {

    @Autowired
    PickRepository pickRepository;

    @Override
    public List<Pick> scoreReadyUnscoredPicks() {
        List<Pick> picks = findPicksReadyToBeScored();
        return awardPointsToPicks(picks);
    }

    @Override
    public List<?> getTotalPointsForYearInLeague(League league, Year year) {
        List<GetPointsOfMembersInLeagueByYearDto> resultSet =
                pickRepository.getPointsOfMembersInLeagueByYear(league, year);
        List<HashMap<String,String>> response = new ArrayList<>();
        resultSet.forEach(result -> {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("username", result.getUser().getUsername());
            hashMap.put("firstName", result.getUser().getFirstName());
            hashMap.put("lastName", result.getUser().getLastName());
            hashMap.put("pointsForYear", result.getPointsForYear() == null ? "0" : result.getPointsForYear().toString());
            response.add(hashMap);
        });
        return response;
    }

    private List<Pick> awardPointsToPicks(List<Pick> picks) {
        picks.forEach(pick -> {
            if (pick.getWinningTeam().equals(pick.getGame().getWinner())) {
                pick.setPointActual(pick.getPointPotential());
            } else {
                pick.setPointActual(0L);
            }
        });
        return pickRepository.saveAll(picks);
    }

    private List<Pick> findPicksReadyToBeScored() {
        return pickRepository.findPicksReadyToBeScored();
    }

}
