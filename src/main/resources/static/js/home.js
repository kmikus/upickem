'use strict'

app.controller('home', function ($scope, $http, $window) {

    // BEGIN INITIALIZAITON
    $scope.error = {
        message: '',
        display: false
    };

    $scope.success = {
        message: '',
        display: false
    };

    $scope.userLeagues = [];
    $scope.selectedLeague;

    $scope.newLeagueClicked = false;
    $scope.selectedUsersInNewLeague = []
    $scope.addLeagueNameInput = '';
    $scope.addLeagueEmailOrUsernameInput = '';
    $scope.maxMembersInLeague = 0;

    $scope.refreshData = function() {
        $scope.getUserLeagues();
        $scope.getMaxMembersInLeague();
    }

    $scope.removeAlerts = function() {
        $scope.error.display = false;
        $scope.success.display = false;
    }

    $scope.getUserLeagues = function() {
        $http.get("/api/user/getLeaguesForLoggedInUser").then(function(response) {
            $scope.userLeagues = response.data.message;
        })
    };

    $scope.getMaxMembersInLeague = function() {
        $http.get("api/league/getMaxMembers").then(function(response) {
            if (response.data.success) {
                $scope.maxMembersInLeague = response.data.message;
            }
        })
    }
    // END INITIALIZATION

    // BEGIN LEAGUE SELECTED
    $scope.selectLeague = function(league) {
        $scope.selectedLeague = league;
    }
    // END LEAGUE SELECTED

    // BEGIN CREATING LEAGUES
    $scope.showNewLeagueArea = function() {
        $scope.newLeagueClicked = true;
    }

    $scope.addUserToSelectedUsersForNewLeague = function() {
        $scope.error.display = false;
        if ($scope.addLeagueEmailOrUsernameInput === '') {
            $scope.error.message = 'Enter a username or email to add';
            $scope.error.display = true;
        } else {
            $http.post("api/user/getUserByEmailOrUsername", $scope.addLeagueEmailOrUsernameInput).then(
                function(response) {
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
    }

    $scope.createNewLeagueSubmit = function() {
        console.log($scope.selectedUsersInNewLeague);
        let requestBody = {
            name: $scope.addLeagueNameInput,
            usernamesOrEmails: $scope.selectedUsersInNewLeague
        };
        $http.post("api/league/create", requestBody).then(
            function(response) {
                if (response.data.success) {
                    $scope.success.message = "League created!"
                    $scope.success.display = true;
                    $scope.newLeagueClicked = false;
                    $scope.refreshData();
                }
            }
        )
    }

    $scope.cancelCreateNewLeague = function() {
        $scope.newLeagueClicked = false;
        $scope.selectedUsersInNewLeague = [];
    }
    // END CREATING LEAGUES

    $scope.resetError = function() {
        $scope.error.display = false;
    }

    $scope.resetSuccess = function() {
        $scope.success.display = false;
    }

    $scope.refreshData();

});