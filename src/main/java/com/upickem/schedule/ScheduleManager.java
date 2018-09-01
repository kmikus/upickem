package com.upickem.schedule;

import com.upickem.model.SeasonType;
import com.upickem.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

@Component
public class ScheduleManager {

    // TODO there was a bug here where the application wouldn't start up and get an array list OOB exception

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    GameService gameService;

    static private Long currentWeek = 1L;
    static private SeasonType currentSeasonType = SeasonType.PRE;

    static public Long getCurrentWeek() {
        return currentWeek;
    }

    static public SeasonType getCurrentSeasonType() {
        return currentSeasonType;
    }

    @Value("${nfl.preseason.endWeek}")
    private Long nflPreseasonEnd;

    @Value("${nfl.regseason.endWeek}")
    private Long nflRegseasonEnd;

    /*
    Every Sunday, Monday, Thursday, and Saturday
    at 12:00AM, 12:00PM, 5:00PM, 9:00PM
    from August to February
     */
    @Scheduled(cron = "0 0 0,12,17,21 * 1-2,8-12 0-1,4,6")
    public void scrape() {
        updateWeekToCurrentWeek();
        log.info("Fired scrape at " + LocalDateTime.now());

        try {
            gameService.saveGames(gameService.pullGameDataFromRemoteServer(
                    Year.now(), currentWeek, currentSeasonType)
            );
        } catch (Exception e) {
            log.error("Could not save game data for " + currentSeasonType +
                    " " + Year.now() + " week " + currentWeek, e);
        }
    }

    // Every Tuesday at 2AM and 2PM from August to February
    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "0 0 2,14 * 1-2,8-12 2")
    public void updateWeekToCurrentWeek() {
        log.info("Fired week update at " + LocalDateTime.now());
        boolean isDateFromServerAhead = true;
        do {
            List<LocalDate> datesToCheck;
            try {
                datesToCheck = gameService.getDatesOfGamesForWeekFromRemote(
                        Year.now(), currentWeek, currentSeasonType);
            } catch (Exception e) {
                log.error("Could not update week to current week", e);
                return;
            }
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

    @Scheduled(cron = "0 0 2 * 5-7 2")
    public void saveAnnualSchedule() {
        log.info("Fired save annual schedule at " + LocalDateTime.now());
        int gamesByYear = 0;

        try {
            gamesByYear = gameService.findGamesByYear(Year.now()).size();
        } catch (Exception e) {
            log.error("Could not fetch the games for " + Year.now(), e);
        }

        if (gamesByYear == 0) {
            log.info("Saving the schedule for " + Year.now());
            try {
                gameService.saveScheduleForYear(Year.now());
            } catch (Exception e) {
                log.error("Could not save schedule for the year", e);
            }
        } else {
            log.info("Games already exist for " + Year.now());
        }
    }

}
