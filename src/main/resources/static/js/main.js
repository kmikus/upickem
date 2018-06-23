'use strict'

var main = angular.module('upickem', []);

main.controller('ctrl', function($scope, $http) {
	
	console.log('hello');
	
	$scope.login = function() {
		var data = {
				'username': $scope.username,
				'password': $scope.password
		};
		
		console.log(data);
		$http.post('http://localhost:8080/api/v1/login', data);
	}
})