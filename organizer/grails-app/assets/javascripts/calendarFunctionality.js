var today = new Date();
var month = today.getMonth();
var userEvents = [];
var token = "";
var eventMonth;
var numberOfDays;
var actual_month;
var month_listing;
var actual_calendar;
var applicationUser;
var shownEvents = [];
var shownEventsIds = [];
var userOrgs =[];
var userOrgEvents = [];
var userFriends = [];
var userFriendsPublicEvents = [];
var allEvents = [];
var showUsersFriendsEvents = false;
var showUsersOrgEvents = false;

Date.prototype.getMonthNames = function() {
    return [ 'January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December' ];
};

Date.prototype.getDaysInMonth = function() {
    return new Date( this.getFullYear(), this.getMonth() + 1, 0 ).getDate();
};

Date.prototype.calendar = function() {
    numberOfDays = this.getDaysInMonth();
    var startingDay = new Date(this.getFullYear(), this.getMonth(), 1).getDay();
    var calendarTable = '<div id="month">' + this.getMonthNames()[this.getMonth()] + '&nbsp;' + this.getFullYear() + '</div>';
    calendarTable += '<table summary="Calendar" class="calendar" style="text-align: center;">';
    calendarTable += '<tr><td colspan="7"></td></tr>';
    calendarTable += '<tr>';
    calendarTable += '<td class="titleColumn">SUN</font></td>';
    calendarTable += '<td class="titleColumn">MON</td>';
    calendarTable += '<td class="titleColumn">TUES</td>';
    calendarTable += '<td class="titleColumn">WED</td>';
    calendarTable += '<td class="titleColumn">THURS</td>';
    calendarTable += '<td class="titleColumn">FRI</td>';
    calendarTable += '<td class="titleColumn">SAT</td></tr>';

    for ( var i = 0; i < startingDay; i++ ) {
        calendarTable += '<td class="dayBlock">&nbsp;</td>';
    }

    var border = startingDay;

    for ( var id = '',  j = 1; j <= numberOfDays; j++ ) {
        if (( month === this.getMonth() ) && ( today.getDate() === j )) {
            id = 'id="current_day"';
        } else {
            id = 'id="day'+j+'"';
        }
        calendarTable += '<td class="dayBlock" ' + id + '>' + j +'</td>'; border++;

        if ((( border % 7 ) === 0 ) && ( j < numberOfDays )) {
            calendarTable += '<tr></tr>';
        }
    }
    while(( border++ % 7)!== 0) {
        calendarTable += '<td>&nbsp;</td>';
    }
    calendarTable += '</table>';
    return calendarTable;
};

/**
 * On load function. Creates and fills the user's calender with events
 */
$(function() {

    $.ajax({
       url: 'user/auth',
       method: 'GET',
        async: false,

        success: function (data) {
            token = data.data.token;
            applicationUser =  data.data.user;

            var userImageDiv = document.getElementById("userImage");
            var newDiv = document.createElement("div");
            newDiv.innerHTML = '<img src="'+applicationUser.userImageUrl+'" class="img-circle" style="width: 60%">';
            userImageDiv.appendChild(newDiv);

        },
        error: function () {
            alert("Could not locate user.");
            window.location.href = '/'
        }
    });
    var selected_month = '<form name="month_holder">';
    selected_month += '<select id="month_items" size="1" onchange="month_picker();">';
    for ( var x = 0; x <= today.getMonthNames().length; x++ ) { selected_month += '<option value="' + today.getMonthNames()[x] + ' 1, ' + today.getFullYear() + '">' + today.getMonthNames()[x] + '</option>'; }
    selected_month += '</select></form>';
    actual_calendar = document.getElementById('show_calendar');
    actual_calendar.innerHTML = today.calendar();
    month_listing = document.getElementById('current_month');
    month_listing.innerHTML = selected_month;
    actual_month = document.getElementById('month_items');
    actual_month.selectedIndex = month;
    eventMonth = today.getMonth()+1;
    controllerFunction()

    //addEvents()
});

/**
 * picks the month to be shown on screen.
 */
function month_picker() {
    var month_menu = new Date(actual_month.value);
    actual_calendar.innerHTML = month_menu.calendar();
    eventMonth = month_menu.getMonth()+1;
    controllerFunction()
}

/**
 * calls the correct methods to get the user's choice of events
 */
function controllerFunction() {
    getEvents();
    if(showUsersFriendsEvents !== false) {
        getUserFriends();
        getUserFriendsEvents();
    }
    if(showUsersOrgEvents !== false) {
        getAllUserOrganizations();
        getAllOrganizationEvents();
    }
    consolidateAllEvents();
    addEvents();
}

var monthFullDate;

/**
 * Gets the user's personal events
 */
function getEvents() {
    $.ajax({
        url: '/user/auth',
        method: "GET",
        async: false,
        success: function (data) {
            token = data.data.token;

            $.ajax({
                url:'api/event/user/month?accessToken='+token+'&monthString='+eventMonth,
                method: "GET",
                async: false,
                success: function (data) {
                    userEvents = data.data.userEvents;
                    allEvents = userEvents;
                },
                error: function () {
                    alert("it didn't work.")
                }
            });
        },
        error: function () {
            alert("Cant Authorize User")
        }
    });
}


/**
 * gets all of the user's friends so that their events can be added to the calendar
 */
function getUserFriends() {

    $.ajax({
       url: 'api/user/following?accessToken='+token,
       method: 'GET',
        async: false,

       success: function (data) {
           userFriends = data.data.users;
       },
       error: function () {
           alert("Couldn't get User's Friends")
       }
    });
}

/**
 * gets all of the events for the user's friends
 */
function getUserFriendsEvents () {

    for(var i=0; i<userFriends.length; i++) {
        $.ajax({
           url: '/api/event/user/friend?accessToken='+token+'&friendId='+userFriends[i].userId+"&monthString="+eventMonth,
           method: 'GET',
            async:false,

           success: function (data) {
               var friendEvents = data.data.userEvents;
               console.log("FRIEND EVENTS:");
               console.log(friendEvents);

               for(var j=0; j<friendEvents.length; j++) {
                   friendEvents[j].eventColor = 'd0d1c8';
                   userFriendsPublicEvents.push(friendEvents[j])
               }
           },
           error: function () {

           }
        });
    }
}

/**
 *
 */
function consolidateAllEvents() {
    if(userFriendsPublicEvents.length > 0) {
        for(var j=0; j<userFriendsPublicEvents.length; j++) {
            allEvents.push(userFriendsPublicEvents[j])
        }
    }
    if(userOrgEvents.length > 0) {
        for(var k=0; k<userOrgEvents.length; k++) {
            allEvents.push(userOrgEvents[k])
        }
    }
    console.log("ALL EVENTS LENGTH " + allEvents.length);
    console.log("ALL EVENTS:");
    console.log(allEvents);
}

/**
 *
 */
function addEvents() {
    var days = [];
    var eventToBeAdded;
    for(var i=1; i<=numberOfDays; i++) {
        var newDiv = document.createElement("div");
        if(today.getDate() !== i) {
            days[i-1] = document.getElementById("day" + i);
        } else {
            days[i-1] = document.getElementById("current_day")
        }

        var numOfEventsForDay = 0;
        for(var e=0; e<allEvents.length; e++) {
            if(allEvents[e].dueDay === i) {
                numOfEventsForDay ++;
            }
        }
        if(numOfEventsForDay>0) {
            if(numOfEventsForDay<2) {
                for (var eventNumber = 0; eventNumber < allEvents.length; eventNumber++) {
                    if (allEvents[eventNumber].dueDay === i) {
                        eventToBeAdded = allEvents[eventNumber];
                        newDiv.innerHTML = insertEventsIntoCalendar(i, eventToBeAdded);
                        days[i - 1].appendChild(newDiv);
                    }
                }
            } else if(numOfEventsForDay > 1){
                newDiv.innerHTML = '<div class="eventDiv" style="background-color: black color: red" onclick="expandEvents()">' +numOfEventsForDay + ' events </div>';
                days[i - 1].appendChild(newDiv);
            }
        }
        numOfEventsForDay = 0;


    }
    allEvents = [];
    userFriendsPublicEvents = [];
}

/**
 *
 * @param i
 * @param eventToBeAdded
 * @returns {string|*}
 */
function insertEventsIntoCalendar(i, eventToBeAdded) {
    var eventString;
    var eventColor = '#';
    eventColor +=eventToBeAdded.eventColor;
    if(eventToBeAdded.ownerId === userEvents[0].ownerId) {
        eventString = '<div style="background-color: ' + eventColor + '" onclick="showEvent()">' + eventToBeAdded.eventName + '</div>';
    } else {
        eventString = '<div class="eventDiv" style="background-color: ' + eventColor + '" onclick="showEvent()">' + eventToBeAdded.eventName + '</div>';
    }
    return eventString

}

/**
 *
 */
function expandEvents() {
    
}

/**
 *
 */
function showEvent() {
    
}

/**
 *
 */
function toggleFriendEvents() {
    if(showUsersFriendsEvents === false) {
        showUsersFriendsEvents = true;
        getUserFriends();
        getUserFriendsEvents();
        allEvents = userFriendsPublicEvents;
        addEvents()
    } else if(showUsersFriendsEvents === true) {
        showUsersFriendsEvents = false;
        var eventDivs = document.getElementsByClassName("eventDiv");
        for(var i = 0; i<eventDivs.length; i++) {
            eventDivs[i].innerHTML = ""
        }
    }

}

/**
 *
 */
function toggleOrgEvents() {
    if(showUsersOrgEvents === false) {
        showUsersOrgEvents = true;
    } else if(showUsersOrgEvents === true) {
        showUsersOrgEvents = false;
    }
}
