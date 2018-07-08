'use strict'

app.controller('login', function($scope, $http, $window) {
    $scope.username;
    $scope.password;

    $scope.signin = function () {
        var signinPayload = {
            'usernameOrEmail': $scope.username,
            'password': $scope.password
        };

        $http.post('/api/auth/signin', signinPayload).then(
            function (value) { console.log(value.status); }
        );
    };

    $scope.registerRedirect = function () {
        $window.location.href = 'register.html';
    };
});