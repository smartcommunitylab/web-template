angular.module('dev', [ 'ngResource','ngCookies'])



/**
 * Main layout controller
 * @param $scope
 */
function MainController($scope, $rootScope, $cookieStore) {
	
	// implicit flow token of the current app
	 $scope.implicitToken = $cookieStore.get('implicitToken');
	 
	  $scope.setToken = function (implicitToken) {
		  $cookieStore.put('implicitToken', implicitToken);
		  $rootScope.$broadcast("implicitToken", implicitToken);
	    };
	    
	  $scope.getImplicitToken = function() {
			
			var win = window.open('https://vas-dev.smartcampuslab.it/aac/eauth/authorize?client_id=a5d402a1-8fcc-46b0-993e-bd97e37eef9c&response_type=token&grant_type=implicit&redirect_uri=http://localhost:8080/web-template/exit');
			win.onload = function() {
				var at = processAuthParams(win.location.hash.substring(1));
				$timeout(function(){
					if (at) {
						setToken(at);					
					} else {
						$scope.info = '';
						$scope.error = 'Problem retrieving the token!';
					}
				},100);
				win.close();
				
			};
			
			
		};
	
}

function SecureController($scope, $location, $cookieStore){
	$scope.implicitToken =$cookieStore.get('implicitToken');
	// implicit flow token of the current app
	$scope.$on("implicitToken", function (event, implicitToken) {
        $scope.implicitToken = implicitToken;
    });
	
}



/**
 * App management controller.
 * @param $scope
 * @param $resource
 * @param $http
 * @param $timeout
 */


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