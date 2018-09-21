angular.module('upickem').service('memberScoreboardService', function($http, $q) {

    this.getMembersInLeague = function(leagueId) {
        let promise = $http.get("/api/leagues/"+leagueId+"/users").then(response => {
            return $q.resolve(response.data);
        });
        return promise;
    };

    this.getPointsByYearForLeague = function(leagueId, year) {
        let promise = $http.get("/api/picks/pointsByYear?leagueId="+leagueId+"&year="+year).then(response => {
            return response.data;
        });
        return promise;
    }
});