package com.upickem.service.Pick;

import com.upickem.model.League;
import com.upickem.model.Pick;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
public interface PickService {

    List<Pick> scoreReadyUnscoredPicks();

    List<?> getTotalPointsForYearInLeague(League league, Year year);
}
