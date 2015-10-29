angular.module('dev', [ 'ngResource','ngCookies'])



/**
 * Main layout controller
 * @param $scope
 */
function MainController($scope, $rootScope, $cookieStore) {	
}

/**
 * Parse authentication parameters obtained from implicit flow authorization request 
 * @param input
 * @returns
 */
function processAuthParams(input) {
	var params = {}, queryString = input;
	var regex = /([^&=]+)=([^&]*)/g;
	while (m = regex.exec(queryString)) {
	  params[m[1]] = m[2];
	}
	return params.access_token;
}