<!DOCTYPE html>
<html lang="en"  >
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

    <script src="lib/jquery.js"></script>
    <script src="lib/bootstrap.min.js"></script>


    </head>

  <body>
  <script language="javascript">
  
  
  function redirect(){
	  top.location.href = "web/";
  }
  
  
	getImplicitToken = function() {
	
	
		var win = window.open('https://vas-dev.smartcampuslab.it/aac/oauth/authorize?client_id=a5d402a1-8fcc-46b0-993e-bd97e37eef9c&response_type=token&grant_type=implicit&redirect_uri=http://localhost:8080/web-template/check');
		win.onload = function() {
			var at = processAuthParams(win.location.hash.substring(1));
			setInterval(function(){
				if (at) {						
					top.location.href = "implicit?token="+at;					
				} else {
					alert("no");
					console.log("no");
				}
			},100);
			win.close();
		};
		
		// top.location.href = "implicit"; //?token="+$scope.implicitToken;
	};
	
	

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
	  
	  
</script>
  
    <div class="container" style="text-align: center;padding-top: 15%;" >
		  
       
                 <button type="submit" class="btn btn-primary" onclick="javascript:redirect();">Going in security</button>
                 

         <a href="#" onClick="getImplicitToken()">Get implicit flow token</a>
       
       
    </div>
  </body>
</html>
