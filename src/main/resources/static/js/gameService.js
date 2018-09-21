angular.module('upickem').service('gameService', function() {
    self = this;

    const CURRENT_DATE = new Date();
    const CURRENT_MONTH = CURRENT_DATE.getMonth();
    const NFL_SEASON_YEAR = CURRENT_MONTH < 4 ? CURRENT_DATE.getFullYear() + 1 : CURRENT_DATE.getFullYear();

    self.seasonYear = NFL_SEASON_YEAR;
});