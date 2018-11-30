<!DOCTYPE html>
<html>
<head>
    <title>Create Event</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="google-signin-client_id" content="578019460076-9h535196sj93sernlo2l09njo22fcn2e.apps.googleusercontent.com">
    <script src="https://apis.google.com/js/platform.js" async defer></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <asset:javascript src="auth/config.js"/>
    <asset:javascript src="auth/logout.js"/>
    <asset:javascript src="eventCreation.js"/>
    <asset:stylesheet src="calendarView.css"/>
    <asset:stylesheet src="eventCreation.css"/>
    <asset:stylesheet src="menuButton.css"/>
    <asset:javascript src="menuButton.js"/>
    <asset:javascript src="getUserPicture.js"/>

</head>
<body>
<div class="intro-header">
    <div>
        <div id="headerGrad">
            <div id="title">
                Create an Event
            </div>
            <span id="userImage" class="otherTitleContent" style="font-size:10px;cursor:pointer" onclick="openNav()">Menu</span>
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
            <div id="userOrgToggle">
                <br>
                <br>
                Please fill out the following Form to create an event.

                <br><br>
                <button onclick="toggleOrgDropdown()" id="organizationEventButton" value="false">
                    <span class="sr-only">Toggle navigation</span> Create an Organization Event<i class="fa fa-bars"></i>
                </button>

                <br><br>
                <!--
                 <div id="orgDropdown">
                 <div class="dropdown">
                        Choose the organization you wish to create an event for from the drop down menu.<br>
                        <button onclick="organizationDropdown()" class="dropbtn">My Organizations</button>
                       <div id="myDropdown" class="dropdown-content">
                           <div id="orgList"></div>
                       </div>
                  </div>
                </div>
                -->

                <div id="orgDropdown" class="required">
                    <div class="textInsideRequired">
                        Enter the id of the organization you would like to create an event for:
                    </div>
                   <input class="textInsideRequired" type="text" name="orgId">&nbsp;*
                </div>
                <br>

                Event Name:<br>
                <div class="required">
                    <input class="textInsideRequired" type="text" id="name">&nbsp;*
                </div>
                <br>

                Description:<br>
                <textarea name="description" id="eventDescription" rows="4" cols="50"></textarea>
                <br><br>

                Starting Date:<br>
                Enter the number value of the month the event will start:<br>
                <input type="text" id="startingMonth"><br>
                Enter the number of the day the event is due:<br>
                <input type="text" id="startingDay"><br>
                Enter the full four digit year the event is due:<br>
                <input type="text" id="startingYear">
                <br><br>

                Due Date:<br>
                Enter the number value of the month the event is due:<br>
                <div class="required">
                    <input class="textInsideRequired" type="text" id="dueMonth">*
                </div>
                Enter the number of the day the event is due:<br>
                <div class="required">
                    <input class="textInsideRequired" type="text" id="dueDay">*
                </div>
                Enter the full four digit year the event is due:<br>
                <div class="required">
                    <input class="textInsideRequired" type="text" id="dueYear">*
                </div>
                <br>

                Hour Due:<br>
                <input type="text" id="dueHour">
                <br><br>

                Minute Due:<br>
                <input type="text" id="dueMinute">
                <br><br>

                Color:<br>
                <input type="color" value="#00bdff" id="color">
                <br><br>

                <div id="privacyRadio">
                    Privacy:<br>
                    <input class="textInsideRequired" type="radio" name="privacyString" value="false" checked> Private<br>
                    <input class="textInsideRequired" type="radio" name="privacyString" value="true"> Public<br>
                    <br><br>
                </div>
                <button id="submitEventButton" onclick="sendEvent()">
                    <span class="sr-only">Toggle navigation</span> Create Event<i class="fa fa-bars"></i>
                </button>
                <br><br><br><br>
            </div>
        </div>
    </div>
</div>
</body>
</html>