package com.upickem.schedule;

public class WeekCounterSingleton {
    // TODO delete this?

    private static WeekCounterSingleton ourInstance = new WeekCounterSingleton();

    public static WeekCounterSingleton getInstance() {
        return ourInstance;
    }

    private WeekCounterSingleton() {
    }

    private Long week = 1L;
    private Boolean isWeekCorrect = false;

    public Long getWeek() {
        return week;
    }

    public void setWeek(Long week) {
        this.week = week;
    }

    public Boolean getWeekCorrect() {
        return isWeekCorrect;
    }

    public void setWeekCorrect(Boolean weekCorrect) {
        isWeekCorrect = weekCorrect;
    }
}
