'use strict'

app.controller('home', function ($scope, $http, $window) {

    // BEGIN INITIALIZAITON
    $scope.error = {
        message: '',
        display: false
    };

    $scope.userLeagues = [];
    $scope.selectedLeague;

    $scope.newLeagueClicked = false;
    $scope.selectedUsersInNewLeague = []
    $scope.addLeagueNameInput = '';
    $scope.addLeagueEmailOrUsernameInput = '';

    $scope.refresh = function() {
        $scope.getUserLeagues();
    }

    $scope.getUserLeagues = function() {
        $http.get("/api/user/getLeaguesForLoggedInUser").then(function(response) {
            $scope.userLeagues = response.data.message;
        })
    };
    // END INITIALIZATION

    // BEGIN LEAGUE SELECTED
    $scope.selectLeague = function(leagueId) {
        $scope.selectedLeague = leagueId;
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
            $http.get("api/user/" + $scope.addLeagueEmailOrUsernameInput).then(
                function(response) {
                    // Server will return true or false
                    if (response.data.success) {
                        $scope.selectedUsersInNewLeague.push(response.data.message)
                    } else {
                        $scope.error.display = true;
                        $scope.error.message = 'User not found';
                    }
                }
            )
        }
    }

    $scope.createNewLeagueSubmit = function() {
        //TODO
    }

    $scope.cancelCreateNewLeague = function() {
        //TODO
    }
    // END CREATING LEAGUES

    $scope.refresh();

});