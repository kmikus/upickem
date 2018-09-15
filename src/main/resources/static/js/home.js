'use strict';

angular.module('upickem').controller('home', function ($scope, $http, $window) {

    const vm = this;

    // TODO seperate selected_league and new_league into different controllers

    // BEGIN CONFIG

    vm.pageNames =  {
        currentWeekPicks: 'currentWeekPicks',
        newLeague: 'newLeague',
        memberScoreboard: 'memberScoreboard'
    };

    vm.contentPage = {
        default: vm.pageNames.currentWeekPicks,
        current: vm.pageNames.currentWeekPicks
    };

    // TODO maybe make this a league setting with default set to no
    const IS_PRESEASON_INCLUDED = false;
    const CURRENT_DATE = new Date();
    const CURRENT_MONTH = CURRENT_DATE.getMonth();
    const NFL_SEASON_YEAR = CURRENT_MONTH < 4 ? CURRENT_DATE.getFullYear() + 1 : CURRENT_DATE.getFullYear();

    // END CONFIG -------------------------------------

    // BEGIN INITIALIZATION
    $scope.error = {
        message: '',
        display: false
    };

    $scope.success = {
        message: '',
        display: false
    };

    $scope.preSeasonEndWeek = 4;
    $scope.regSeasonEndWeek = 17;
    $scope.nflSkipWeek = 21;
    $scope.nflEndWeek = 22;

    $scope.userLeagues = [];
    $scope.selectedLeague = null;
    $scope.currentWeekNum = null;
    $scope.currentWeekGames = null;
    $scope.currentWeekSeasonType = null;
    $scope.selectedWeek = null;
    $scope.weekSelectionList = [];
    $scope.selectedGames = null;
    $scope.selectedGamesRadio = [];

    $scope.newLeagueClicked = false;
    $scope.selectedUsersInNewLeague = [];
    $scope.addLeagueNameInput = '';
    $scope.addLeagueEmailOrUsernameInput = '';
    $scope.maxMembersInLeague = 0;
    $scope.nowTime = new Date();

    $scope.refreshData = function () {
        $scope.getUserLeagues();
        $scope.getMaxMembersInLeague();
        $scope.getCurrentWeekAndGames();
    };

    $scope.resetMainArea = function () {
        $scope.newLeagueClicked = false;
        $scope.selectedLeague = null;
    };

    $scope.removeAlerts = function () {
        $scope.error.display = false;
        $scope.success.display = false;
    };

    $scope.getUserLeagues = function () {
        $http.get("/api/users/getLeaguesForLoggedInUser").then(function (response) {
            $scope.userLeagues = response.data.message;
        })
    };

    // TODO migrate above function to service based
    function getUserLeagueData() {
        return $http.get("/api/users/getLeaguesForLoggedInUser");
    }

    $scope.getMaxMembersInLeague = function () {
        $http.get("api/leagues/getMaxMembers").then(function (response) {
            if (response.data.success) {
                $scope.maxMembersInLeague = response.data.message;
            }
        })
    };

    $scope.getCurrentWeekAndGames = function () {
        $http.get("api/games/currentWeek").then(function (response) {
            if (response.data.success) {
                $scope.currentWeekNum = response.data.message.week;
                $scope.currentWeekGames = response.data.message.games;
                $scope.currentWeekGames.forEach((game, i) => {
                    let dateFormatted = moment(game.dateAndTime).format("ddd M/D h:mm A");
                    $scope.currentWeekGames[i].dateFormatted = (dateFormatted);
                });
                $scope.currentWeekSeasonType = response.data.message.seasonType;
                $scope.generateFullWeekSelectionList(IS_PRESEASON_INCLUDED);
                $scope.createModelForSelectedGamesRadioButtons();
            }
        })
    };

    // END INITIALIZATION -------------------------------------

    // BEGIN LEAGUE SELECTED
    $scope.selectLeague = function (league) {
        $scope.selectedLeague = league;
        $scope.newLeagueClicked = false;
        $scope.updateGameDataOnSelectedWeekChange();
    };

    $scope.generateFullWeekSelectionList = function (includePreseason = false) {
        $scope.weekSelectionList = [];
        if (includePreseason) {
            for (let i = 1; i <= $scope.preSeasonEndWeek; i++) {
                $scope.weekSelectionList.push({
                    seasonType: 'PRE',
                    number: i,
                    displayVal: 'PRE ' + i
                });
            }
        }
        for (let i = 1; i <= $scope.regSeasonEndWeek; i++) {
            $scope.weekSelectionList.push({
                seasonType: 'REG',
                number: i,
                displayVal: i
            })
        }
        if ($scope.currentWeekNum > 17) {
            $scope.weekSelectionList.push({
                seasonType: 'POST',
                number: 18,
                displayVal: 'Wild Card'
            })
        }
        if ($scope.currentWeekNum > 18) {
            $scope.weekSelectionList.push({
                seasonType: 'POST',
                number: 19,
                displayVal: 'Divisional'
            })
        }
        if ($scope.currentWeekNum > 19) {
            $scope.weekSelectionList.push({
                seasonType: 'POST',
                number: 20,
                displayVal: 'Conference'
            })
        }
        if ($scope.currentWeekNum > 20) {
            $scope.weekSelectionList.push({
                seasonType: 'POST',
                number: 22,
                displayVal: 'Super Bowl'
            })
        }

        $scope.initializeWeekSelectedToCurrentWeek();
    };

    $scope.updateGameDataOnSelectedWeekChange = function () {
        let url = "/api/games?year=" + NFL_SEASON_YEAR +
            "&week=" + $scope.selectedWeek.number +
            "&seasonType=" + $scope.selectedWeek.seasonType;
        $http.get(url).then(function (response) {
            if (response.data.success) {
                $scope.selectedGames = response.data.message;
                $scope.selectedGames.forEach((game, i) => {
                    let dateFormatted = moment(game.dateAndTime).format("ddd M/D h:mm A");
                    $scope.selectedGames[i].dateObj = new Date(game.dateAndTime);
                    $scope.selectedGames[i].dateFormatted = (dateFormatted);
                });
                $scope.createModelForSelectedGamesRadioButtons();
            }
        });
    };

    $scope.initializeWeekSelectedToCurrentWeek = function () {
        $scope.selectedWeek = $scope.weekSelectionList.find(function (element) {
            return (element.number === $scope.currentWeekNum && element.seasonType === $scope.currentWeekSeasonType)
        });
        if ($scope.selectedWeek == null) {
            $scope.selectedWeek = $scope.weekSelectionList[0];
        }
        $scope.selectedGames = $scope.currentWeekGames;
    };

    $scope.createModelForSelectedGamesRadioButtons = function () {
        $scope.selectedGamesRadio = [];
        $scope.selectedGames.forEach(function (element) {
            $scope.selectedGamesRadio.push(
                {
                    gameId: element.gameId,
                    value: null
                }
            )
        });
        if ($scope.selectedLeague != null) {
            let url = "api/picks?leagueId=" + $scope.selectedLeague.leagueId + "&year=" +
                NFL_SEASON_YEAR + "&week=" + $scope.selectedWeek.number;
            $http.get(url).then(function (response) {
                $scope.picksForWeek = response.data.message;
                $scope.picksForWeek.forEach((pick) => {
                    let i = $scope.selectedGamesRadio.findIndex(radio => radio.gameId === pick.game.gameId);
                    $scope.selectedGamesRadio[i].value = pick.winningTeam;
                })
            })
        }
    };

    $scope.submitPicks = function () {
        let requestBody = {
            leagueId: $scope.selectedLeague.leagueId,
            gameIdsAndWinners: []
        };
        $scope.selectedGamesRadio.forEach(function (element, i) {
            let nowTime = new Date();
            if (element.value != null && nowTime < $scope.selectedGames[i].dateObj) {
                requestBody.gameIdsAndWinners.push(element);
                $scope.success.display = true;
                $scope.success.message = requestBody.gameIdsAndWinners.length + ' picks submitted.';
            }
        });
        $http.post("/api/picks/create", requestBody);
    };

    // END LEAGUE SELECTED -------------------------------------

    // BEGIN CREATING LEAGUES
    $scope.showNewLeagueArea = function () {
        vm.contentPage.default = vm.pageNames.newLeague;
        $scope.selectedLeague = null;
    };

    $scope.addUserToSelectedUsersForNewLeague = function () {
        $scope.error.display = false;
        if ($scope.addLeagueEmailOrUsernameInput === '') {
            $scope.error.message = 'Enter a username or email to add';
            $scope.error.display = true;
        } else {
            $http.post("api/users/getUserByEmailOrUsername", $scope.addLeagueEmailOrUsernameInput).then(
                function (response) {
                    // Server will return true or false
                    if (response.data.success) {
                        $scope.selectedUsersInNewLeague.push(response.data.message.username)
                    } else {
                        $scope.error.display = true;
                        $scope.error.message = 'User not found';
                    }
                }
            )
        }
    };

    $scope.createNewLeagueSubmit = function () {
        let requestBody = {
            name: $scope.addLeagueNameInput,
            usernamesOrEmails: $scope.selectedUsersInNewLeague
        };
        $http.post("api/leagues/create", requestBody).then(
            function (response) {
                if (response.data.success) {
                    $scope.success.message = "League created!"
                    $scope.success.display = true;
                    $scope.newLeagueClicked = false;
                    $scope.refreshData();
                }
            }
        )
    };

    $scope.cancelCreateNewLeague = function () {
        $scope.selectedUsersInNewLeague = [];
    };
    // END CREATING LEAGUES --------------------------------------

    $scope.resetError = function () {
        $scope.error.display = false;
    };

    $scope.resetSuccess = function () {
        $scope.success.display = false;
    };

    (function() {
        getUserLeagueData().then(function(response) {
            $scope.getMaxMembersInLeague();
            $scope.getCurrentWeekAndGames();
            $scope.userLeagues = response.data.message;
            if ($scope.userLeagues.length > 0) {
                $scope.selectLeague($scope.userLeagues[0]);
                vm.contentPage.current = pageNames.currentWeekPicks;
            } else {
                vm.contentPage = pageNames.newLeague;
            }
        })
    })();

});