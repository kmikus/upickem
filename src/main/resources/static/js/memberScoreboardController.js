angular.module('upickem').controller('memberScoreboardController',
    ['memberScoreboardService', 'leagueService', 'gameService', '$q',
        function (memberScoreboardService, leagueService, gameService, $q) {
            const vm = this;

            let league = leagueService.selectedLeague;
            let seasonYear = gameService.seasonYear;
            memberScoreboardService.getMembersInLeague(league.leagueId).then(response => {
                vm.memberData = response.message;
            });
            memberScoreboardService.getPointsByYearForLeague(league.leagueId, seasonYear).then(response => {
                vm.pointsData = response.message;
                vm.scoreboardData = vm.memberData.map(
                    x => Object.assign(x, vm.pointsData.find(y => y.username === x.username)));
            });

        }]);