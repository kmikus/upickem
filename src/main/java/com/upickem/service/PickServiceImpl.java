package com.upickem.service;

import com.upickem.model.Pick;
import com.upickem.repository.PickRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private List<Pick> awardPointsToPicks(List<Pick> picks) {
        picks.forEach(pick -> {
            if (pick.getWinningTeam().equals(pick.getGame().getWinner())) {
                pick.setPointActual(pick.getPointPotential());
            } else {
                pick.setPointActual(0L);
            }
        });
        return pickRepository.save(picks);
    }

    private List<Pick> findPicksReadyToBeScored() {
        return pickRepository.findPicksReadyToBeScored();
    }

}
