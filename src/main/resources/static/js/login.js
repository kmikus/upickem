'use strict'

app.controller('login', function($scope, $http, $window) {
    $scope.username;
    $scope.password;
    $scope.badUsernamePasswordEntered = false;

    $scope.signin = function () {
        var signinPayload = {
            'usernameOrEmail': $scope.username,
            'password': $scope.password
        };

        $http.post('/api/auth/signin', signinPayload).then(
            function (response) {
                if (response.status === 200) {
                    $scope.badUsernamePasswordEntered = false;
                    $window.location.href = 'home.html';
                } else {
                    // TODO intercept the 401 received to display error messages
                    $scope.badUsernamePasswordEntered = true;
                }
            }
        );
    };

    $scope.registerRedirect = function () {
        $window.location.href = 'register.html';
    };
});