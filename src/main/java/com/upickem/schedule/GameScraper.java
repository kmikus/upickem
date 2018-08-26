package com.upickem.schedule;

import com.upickem.service.GameService;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class GameScraper {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    GameService gameService;

    //TODO make this run only during the nfl season
    //TODO change this to run only in september
    @Scheduled(cron = "0 0 0,12,17,21 * 1-2,8-12 0-1,4,6")
    public void scrape() {
        log.info("Fired scrape at " + LocalDateTime.now());
        Long week = WeekCounterSingleton.getInstance().getWeek();

        if (!WeekCounterSingleton.getInstance().getFirstScrapeCompleted()) {
            // First scrape, save the records
        } else  {
            // After first scrape, update the records
        }

    }

    @Scheduled(cron = "0 0 2 * 1-2,8-12 2")
    public void updateWeekAndResetFirstScrapeFlag() {
        log.info("Fired updateWeekAndResetFirstScrapeFlag at " + LocalDateTime.now());
    }

}
