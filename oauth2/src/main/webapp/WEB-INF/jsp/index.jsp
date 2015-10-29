<!DOCTYPE html>
<html lang="en" ng-app="dev">
<head>
<meta charset="utf-8">
<title>SmartCommunity Developers</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/bs-ext.css" rel="stylesheet">
<link href="css/bootstrap-responsive.min.css" rel="stylesheet">
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.7/angular.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.7/angular-resource.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.7/angular-cookies.min.js"></script>
<script src="lib/jquery.js"></script>
<script src="lib/bootstrap.min.js"></script>
<script src="js/services.js"></script>

</head>

<body>
	<script>
		function redirect() {
			top.location.href = "web/";
		}
	</script>

	<div ng-controller="MainController" class="container">

		<div class="row">
			<div class="span4 offset4" style="padding-top: 2em;">
				<button type="submit" class="btn btn-primary btn-large"
					onclick="javascript:redirect();">Who I am</button>
			</div>
		</div>

	</div>
</body>
</html>
