package com.upickem.schedule;

import com.upickem.model.SeasonType;
import com.upickem.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

@Component
public class GameScraper {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    GameService gameService;

    private Long currentWeek = 1L;
    private SeasonType currentSeasonType = SeasonType.PRE;

    @Value("${nfl.preseason.endWeek}")
    private Long nflPreseasonEnd;

    @Value("${nfl.regseason.endWeek}")
    private Long nflRegseasonEnd;

    /*
    Every Sunday, Monday, Thursday, and Saturday
    at 12:00AM, 12:00PM, 5:00PM, 9:00PM
    from August to February
     */
//    @Scheduled(cron = "0 0 0,12,17,21 * 1-2,8-12 0-1,4,6")
//    @Scheduled(cron = "0,14,29,44 * * * * *")
    public void scrape() {
        log.info("Fired scrape at " + LocalDateTime.now());

        // Todo get data if current week doesn't match up to date from db
    }

    // Every Tuesday at 2AM and 2PM from August to February
    @Scheduled(cron = "0 0 2,14 * 1-2,8-12 2")
    public void updateWeekToCurrentWeek() {
        log.info("Fired week update at " + LocalDateTime.now());
        boolean isDateFromServerAhead = true;
        do {
            List<LocalDate> datesToCheck = gameService.getDatesOfGamesForWeekFromRemote(
                    Year.now(), currentWeek, currentSeasonType);
            if (datesToCheck.get(datesToCheck.size() - 1).isBefore(LocalDate.now())) {
                // TODO switch to case statement
                if (currentWeek == nflPreseasonEnd) {
                    currentSeasonType = SeasonType.REG;
                    currentWeek = 1L;
                } else if (currentWeek == nflRegseasonEnd) {
                    currentSeasonType = SeasonType.POST;
                    currentWeek++;
                } else if (currentWeek == nflRegseasonEnd - 1) {
                    currentWeek = nflRegseasonEnd;
                } else {
                    currentWeek++;
                }
                log.info("week? : " + currentWeek);
            } else {
                isDateFromServerAhead = false;
            }

        } while (isDateFromServerAhead);

        log.info("Current week is: " + currentWeek + " and current season type is: " + currentSeasonType);
    }

}
