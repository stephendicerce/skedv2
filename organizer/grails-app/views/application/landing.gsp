<!DOCTYPE html>
<html lang="en">
<head>

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="google-signin-client_id" content="578019460076-9h535196sj93sernlo2l09njo22fcn2e.apps.googleusercontent.com">
    <script src="https://apis.google.com/js/platform.js" async defer></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <!--<link rel="icon" href="${resource(directory: 'images', file: 'logo.ico')}"/>-->
    <title>Sked</title>
    <asset:javascript src="auth/config.js"/>
    <asset:javascript src="auth/login.js"/>
    <asset:javascript src="auth/logout.js"/>
    <asset:javascript src="calendarFunctionalityWithoutEvents.js"/>
    <asset:javascript src="menuButton.js"/>
    <asset:stylesheet src="menuButton.css"/>
    <asset:stylesheet src="calendarView.css"/>
    <style>

        .data{
            display: none;
        }
    </style>


</head>

<body class="bg-light-gray">
<div class="intro-header">
    <div class="container">

        <div class="row">
            <div class="col-lg-12">
                <div id="viewBorder">
                <div class="intro-message">
                        <div id="headerGrad">
                            <span id="title">
                                SKED
                            </span>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <span class="otherTitleContent" style="font-size:30px;cursor:pointer" onclick="openNav()">&#9776;</span><br><br><br><br><br><br>
                            <span class="otherTitleContent">
                                <div id="sign-in-btn"></div>
                            </span>

                            <div id="mySidenav" class="sidenav">
                                <div content="menuContentHTML"></div>
                            </div>

                        </div>
                </div>
                    <div id="description">
                        An everyday task manager to be used by you and your friends.
                        Keep up to date with everyone's plans, as well as keeping on
                        schedule with your own.
                    </div>
                    <p>&nbsp</p>
                    <div id="show_calendar">&nbsp;</div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>


</html>