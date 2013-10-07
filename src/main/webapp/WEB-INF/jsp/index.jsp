<!DOCTYPE html>
<html lang="en" ng-app="dev" >
  <head>
    <meta charset="utf-8">
    <title>SmartCampus Developers</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
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
  <script language="javascript">
  
  
  function redirect(){
	  top.location.href = "web/";
  }
  
  
	
	
	

	
	  
	  
</script>
  
    <div ng-controller="MainController" class="container" style="text-align: center;padding-top: 5%;" >
		  
       
                 <button type="submit" class="btn btn-primary" onclick="javascript:redirect();">Going in security</button>
                 

         <a href="#" ng-click="getImplicitToken()">Get implicit flow token</a>
       
       
    </div>
    <div ng-controller="SecureController" class="container" style="text-align: center;padding-top: 15%;">
		<h1>Session parameters</h1>



		<form ng-submit="">
			<fieldset>
				<legend> UserData </legend>

				
				<div class="row-fluid">
					<div class="span3 ">
						<strong>Token</strong>
					</div>
					<div class="span5 "> {{implicitToken}}</div>
					<div class="span4 "></div>
				</div>


			</fieldset>
		</form>
	</div>
    
  </body>
</html>
