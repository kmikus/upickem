'use strict'

app.controller('register', function($scope, $http, $window) {
    $scope.firstName;
    $scope.lastName;
    $scope.username;
    $scope.email;
    $scope.password;

    $scope.signup = function() {
        var signupPayload = {
            'firstName': $scope.firstName,
            'lastName': $scope.lastName,
            'username': $scope.username,
            'email': $scope.email,
            'password': $scope.password
        };

        $http.post('/api/auth/signup', signupPayload).then(
            function (response) {
                if (response.data.success === true) {
                    $window.location.href = 'login.html';
                }
            }
        );
    }

    $scope.loginRedirect = function() {
        $window.location.href = 'login.html';
    }
});