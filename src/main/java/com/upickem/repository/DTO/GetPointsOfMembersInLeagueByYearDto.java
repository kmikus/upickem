package com.upickem.repository.DTO;

import com.upickem.model.User;

public class GetPointsOfMembersInLeagueByYearDto {
    private User user;
    private Long pointsForYear;

    public GetPointsOfMembersInLeagueByYearDto() {
    }

    public GetPointsOfMembersInLeagueByYearDto(User user, Long pointsForYear) {
        this.user = user;
        this.pointsForYear = pointsForYear;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getPointsForYear() {
        return pointsForYear;
    }

    public void setPointsForYear(Long pointsForYear) {
        this.pointsForYear = pointsForYear;
    }
}
