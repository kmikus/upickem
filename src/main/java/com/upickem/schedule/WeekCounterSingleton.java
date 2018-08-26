package com.upickem.schedule;

public class WeekCounterSingleton {
    private static WeekCounterSingleton ourInstance = new WeekCounterSingleton();

    public static WeekCounterSingleton getInstance() {
        return ourInstance;
    }

    private WeekCounterSingleton() {
    }

    private Long week = 1L;
    private Boolean isFirstScrapeCompleted = false;

    public Long getWeek() {
        return week;
    }

    public void setWeek(Long week) {
        this.week = week;
    }

    public Boolean getFirstScrapeCompleted() {
        return isFirstScrapeCompleted;
    }

    public void setFirstScrapeCompleted(Boolean firstScrapeCompleted) {
        isFirstScrapeCompleted = firstScrapeCompleted;
    }
}
