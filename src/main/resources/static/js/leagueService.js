angular.module('upickem').service('leagueService', function() {
    self = this;

    self.selectedLeague = {leagueId: 1};

    self.setSelectedLeague = function(league) {
        self.selectedLeague = league;
    };
});