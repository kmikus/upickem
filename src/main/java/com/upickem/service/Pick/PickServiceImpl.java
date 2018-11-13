package com.upickem.service.Pick;

import com.upickem.model.League;
import com.upickem.model.Pick;
import com.upickem.repository.DTO.GetPointsOfMembersInLeagueByYearDto;
import com.upickem.repository.PickRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class PickServiceImpl implements PickService {

    private Logger logger = LoggerFactory.getLogger(PickServiceImpl.class);

    @Autowired
    PickRepository pickRepository;

    @Override
    public List<Pick> scoreReadyUnscoredPicks() {
        List<Pick> picks = findPicksReadyToBeScored();
        logger.info(String.format("Found %s picks ready to be scored", picks.size()));
        return awardPointsToPicks(picks);
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
