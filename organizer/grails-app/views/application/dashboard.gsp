<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="google-signin-client_id" content="578019460076-9h535196sj93sernlo2l09njo22fcn2e.apps.googleusercontent.com">
    <script src="https://apis.google.com/js/platform.js" async defer></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <asset:javascript src="auth/config.js"/>
    <asset:javascript src="auth/logout.js"/>
    <asset:javascript src="calendarFunctionality.js"/>
    <asset:stylesheet src="calendarView.css"/>
    <asset:javascript src="menuButton.js"/>
    <asset:stylesheet src="menuButton.css"/>

</head>
<body>
<div class="intro-header">
    <div>

        <div class="row">
            <div class="col-lg-12">
                <div id="viewBorder">
                    <div id=headerGrad class="intro-message">
                        <div id="title" onclick="getEvents()">
                            Dashboard
                        </div>
                        <span id="userImage" class="otherTitleContent" style="font-size:10px;cursor:pointer" onclick="openNav()">MENU:</span>
                        <br><br><br><br><br><br><br>
                        <div class="otherTitleContent">
                            Toggle Friend Events On/Off
                            <label class="switch">
                                <input type="checkbox" onclick="toggleFriendEvents()">
                                <span class="slider round"></span>
                            </label>
                            <button type="button" onclick="document.location.href='../createEvent'">
                                <span class="sr-only">Toggle navigation</span> NEW EVENT <i class="fa fa-bars"></i>
                            </button>
                            <button type="button" onclick="document.location.href='../createOrganization'">
                                <span class="sr-only">Toggle navigation</span> CREATE ORGANIZATION <i class="fa fa-bars"></i>
                            </button>
                            <button type="button" onclick="logout()">
                                <span class="sr-only">Toggle navigation</span> LOGOUT <i class="fa fa-bars"></i>
                            </button>
                        </div>
                        <div id="mySidenav" class="sidenav">
                        <div content="menuContentHTML"></div>
                        </div>


                    </div>
                    <div class="container">
                        <p>&nbsp</p>
                        <div id="current_month">&nbsp;</div>
                        <div id="show_calendar">&nbsp;</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>