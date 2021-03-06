<!DOCTYPE html>
<html>
<head>
    <title>Create Organization</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="google-signin-client_id" content="578019460076-9h535196sj93sernlo2l09njo22fcn2e.apps.googleusercontent.com">
    <script src="https://apis.google.com/js/platform.js" async defer></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <asset:javascript src="auth/config.js"/>
    <asset:javascript src="auth/logout.js"/>
    <asset:javascript src="organizationCreation.js"/>
    <asset:javascript src="menuButton.js"/>
    <asset:stylesheet src="calendarView.css"/>
    <asset:stylesheet src="eventCreation.css"/>
    <asset:stylesheet src="menuButton.css"/>
    <asset:stylesheet src="organizationCreation.css"/>
    <asset:javascript src="getUserPicture.js"/>

</head>
<body>
<div class="intro-header">
    <div>
        <div id="headerGrad" class="intro-header">
            <div class="orgTitle">
                Create an Organization
            </div>
            <span id="userImage" class="otherTitleContent" style="font-size:10px;cursor:pointer" onclick="openNav()">MENU:</span>
            <br><br><br><br><br><br><br>
            <div class="otherTitleContent">
                <button type="button" onclick="document.location.href='../dashboard'">
                    <span class="sr-only">Toggle navigation</span> Dashboard <i class="fa fa-bars"></i>
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
            <br>
            <div id="summary">
                Please fill out the following to create a new organization.<br><br>

                Name:<br>
                <div class="required">
                    <input class="textInsideRequired" type="text" id="name">*
                </div>
                <br>

                Description:<br>
                <div class="required">
                    <textarea class="textInsideRequired" id="orgDescription"></textarea>*
                </div>
                <br><br>

                <button id="submitOrgButton" onclick="sendOrg()">
                    <span class="sr-only">Toggle navigation</span> Create Organization<i class="fa fa-bars"></i>
                </button>
            </div>
        </div>
    </div>
</div>
</body>
</html>