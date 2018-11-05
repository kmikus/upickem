angular.module('upickem').controller('memberScoreboardController',
    ['memberScoreboardService', 'leagueService', 'gameService', '$q',
        function (memberScoreboardService, leagueService, gameService, $q) {
            const vm = this;

            let league = leagueService.selectedLeague;
            let seasonYear = gameService.seasonYear;
            memberScoreboardService.getPointsByYearForLeague(league.leagueId, seasonYear).then(response => {
                vm.pointsData = response.message;
            });

        }]);