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
<style type="text/css">
body {
	padding-top: 60px;
	padding-bottom: 40px;
}

.sidebar-nav {
	padding: 9px 0;
}
</style>
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


	<div ng-controller="SecureController" class="container">
		<h1>Welcome in security Oauth2</h1>

		<fieldset>
			<legend>UserData </legend>

			<div class="row-fluid">
				<div class="span3 ">
					<strong>Name</strong>
				</div>
				<div class="span5 "><%=request.getAttribute("name")%></div>
				<div class="span4 "></div>
			</div>
			<div class="row-fluid">
				<div class="span3 ">
					<strong>Surname</strong>
				</div>
				<div class="span5 "><%=request.getAttribute("surname")%></div>
				<div class="span4 "></div>
			</div>
			<div class="row-fluid">
				<div class="span3 ">
					<strong>Token</strong>
				</div>
				<div class="span5 "><%=request.getAttribute("token")%></div>
				<div class="span4 "></div>
			</div>
		</fieldset>
	</div>
</body>
</html>
