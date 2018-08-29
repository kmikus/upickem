package com.upickem.payload;

import com.upickem.model.SeasonType;

public class GameRequest {

    private Long calendarYear;

    private Long nflWeek;

    private SeasonType seasonType;

    public Long getCalendarYear() {
        return calendarYear;
    }

    public void setCalendarYear(Long calendarYear) {
        this.calendarYear = calendarYear;
    }

    public Long getNflWeek() {
        return nflWeek;
    }

    public void setNflWeek(Long nflWeek) {
        this.nflWeek = nflWeek;
    }

    public SeasonType getSeasonType() {
        return seasonType;
    }

    public void setSeasonType(SeasonType seasonType) {
        this.seasonType = seasonType;
    }
}
