var userEvents = [];
var token = '';


$(function () {
   getAllEvents()
});

function getAllEvents() {
    $.ajax({
        url: '/user/auth',
        method: "GET",

        success: function (data) {
            token = data.data.token;

            var urlString = 'api/event/user/all?accessToken='+token;
            $.ajax({
                url: urlString,
                method: "GET",

                success: function (data) {
                    userEvents = data.data.userEvents;
                    var i;
                    var eventsDiv = document.getElementById("eventList");
                    if(userEvents.length === 0) {
                        var string = "<p style=\"text-align: center;\">You currently have no stored events.</p>";
                        var div = document.createElement("div");
                        div.innerHTML = string;
                        eventsDiv.appendChild(div);
                    }
                    for (i = 0; i < userEvents.length; i++) {
                        var description;
                        if(userEvents[i].eventDescription === null){
                            description = "This event has no description"
                        } else {
                            description = userEvents[i].eventDescription
                        }
                        var publicOrPrivate;
                        if(userEvents[i].isPublic === 1) {
                            publicOrPrivate = "private"
                        } else if(userEvents[i].isPublic === 0) {
                            publicOrPrivate = "public"
                        }

                        var htmlString = eventHTML(userEvents[i].eventName,description, userEvents[i].dueMonth, userEvents[i].dueDay, userEvents[i].dueYear, publicOrPrivate, userEvents[i].eventId, userEvents[i].eventColor);
                        var newDiv = document.createElement("div");
                        newDiv.innerHTML = htmlString;
                        eventsDiv.appendChild(newDiv);
                    }
                }
            });
        }
    });
}

var idName = 0;
function eventHTML(eventName, description, dueMonth, dueDay, dueYear, publicOrPrivate, eventId, eventColor) {
    var colorString = "#"+eventColor;
    idName++;
    //var str = '<div class="col-md-4 col-sm-6 portfolio-item" style="box-shadow: 0px 0px 0px gray; padding: 20px;">'
    var str = '<div class="userEvent col-md-4 col-sm-6 portfolio-item" id="event'+idName+'" style="border:'+colorString+' solid">';
    //var str = '<div class="col-md-4 col-sm-6 portfolio-item" style="box-shadow: 0px 0px 0px gray; padding: 10px;">'
    str += '<div class="portfolio-hover">';
    str += '<div class="portfolio-hover-content">';
    str += '<i class="fa fa-plus fa-3x"></i></div></div>';

    //str += '<asset:image class="img-responsive" src="logo.png" alt=""/>'
    //str += '<img src="assets/images/startup-framework.png" class="img-responsive" alt=""></a>'
    str += '<div class="portfolio-caption"><h4>' +eventName +'</h4><p class="text-muted"> Description: '+ description + '<br>Month Due: '+dueMonth+ '<br>Day Due: '+dueDay+'<br>Year Due:'+ dueYear+'<br>This event is '+publicOrPrivate+'</p>' +
        '<button type="button" onclick="editEvent('+eventId+')">\n' +
        '<span class="sr-only">Toggle navigation</span> Edit <i class="fa fa-bars"></i>\n' +
        '</button>' + '&nbsp;' +
        '<button type="button" onclick="deleteEvent('+eventId+')">\n' +
        '<span class="sr-only">Toggle navigation</span> Delete <i class="fa fa-bars"></i>\n' +
        '</button></div></div>';
    return str
}

function deleteEvent(eventId) {
    console.log(eventId);
    $.ajax({
        url: '/user/auth',
        method: "GET",

        success: function (data) {
            token = data.data.token;
            //console.log(token);
            $.ajax({
                url: 'api/event?accessToken=' + token + '&eventId=' + eventId,
                method: "DELETE",
                success: function () {
                    window.location.reload()
                },
                error: function () {
                    alert("Event could not be deleted, please try again");
                    getAllEvents()
                }
            });
        }
    });
}

var eventName;
var eventDescription;
var startingMonth;
var startingDay;
var startingYear;
var dueMonth;
var dueDay;
var dueYear;
var dueHour;
var dueMinute;
var eventColor;
var isPublic;

function editEvent(eventId) {
    var userEvents = document.getElementById("eventList");
    userEvents.style.display = "none";
    var eventToBeEdited;


    $.ajax({
        url: 'user/auth',
        method: "GET",

        success: function (data) {
            token = data.data.token;
            $.ajax({
               url: 'api/event/user?accessToken='+token+'&eventId='+eventId,
               method: "GET",

               success: function (data) {
                   eventToBeEdited= data.data.event;
                   eventName = eventToBeEdited.eventName;
                   if(eventToBeEdited.eventDescription !== null) {
                       eventDescription = eventToBeEdited.eventDescription;
                   } else {
                       eventDescription = "This event has no description"
                   }
                   startingMonth = eventToBeEdited.startingMonth;
                   startingDay = eventToBeEdited.startingMonth;
                   startingYear = eventToBeEdited.startingYear;
                   dueMonth = eventToBeEdited.dueMonth;
                   dueDay = eventToBeEdited.dueDay;
                   dueYear = eventToBeEdited.dueYear;
                   dueHour = eventToBeEdited.dueHour;
                   dueMinute = eventToBeEdited.dueMinute;
                   eventColor = eventToBeEdited.eventColor;
                   isPublic = eventToBeEdited.isPublic;

                   var editEventDiv = document.getElementById("editEventDiv");

                   var editEventString = 'Edit the input fields you wish to change<br>'+
                       'Event Name:<br>'+
                       '<input id="newName" type="text" value="'+eventName+'"><br><br>' +
                       'Description:<br>' +
                       '<textarea id="newDescription" rows="4" cols="50">'+eventDescription+'</textarea><br><br>' +
                       'Starting Month:<br>' +
                       '<input id="newStartingMonth" type="text" value="'+startingMonth+'"><br><br>' +
                       'Starting Day:<br>' +
                       '<input id="newStartingDay" type="text" value="'+startingDay+'"><br><br>' +
                       'Starting Year:<br>' +
                       '<input id="newStartingYear" type="text" value="'+startingYear+'"><br><br>' +
                       'Due Month:<br>' +
                       '<input id="newDueMonth" type="text" value="'+dueMonth+'"><br><br>' +
                       'Due Day:<br>' +
                       '<input id="newDueDay" type="text" value="'+dueDay+'"><br><br>' +
                       'Due Year:<br>' +
                       '<input id="newDueYear" type="text" value="'+dueYear+'"><br><br>' +
                       'Due Hour:<br>' +
                       '<input id="newDueHour" type="text" value="'+dueHour+'"><br><br>' +
                       'Due Minute:<br>' +
                       '<input id="newDueMinute" type="text" value="'+dueMinute+'"><br><br>' +
                       'Color:<br>' +
                       '<input id="newColor" type="color" value="#'+eventColor+'"><br><br>' +
                       '<div id="privacyRadio">' +
                       'Privacy:<br>' +
                       '<input class="textInsideRequired" type="radio" name="privacyString" value="false"> Private<br>' +
                       '<input class="textInsideRequired" type="radio" name="privacyString" value="true"> Public<br>' +
                       '<br><br>' +
                       '<button type="button" onclick="checkEventChanges('+eventId+')">' +
                       '<span class="sr-only">Toggle navigation</span> Submit Changes <i class="fa fa-bars"></i>' +
                       '</button>' +
                       '</div>';

                   var newDiv = document.createElement("div");
                   newDiv.innerHTML = editEventString;
                   editEventDiv.appendChild(newDiv);


               },
                error: function () {

                }
            });

        }
    });


}

function checkEventChanges(eventId) {
    var newName = null;
    if(document.getElementById("newName").value !== null) {
        if(document.getElementById("newName").value === eventName) {
            newName = eventName
        } else {
            newName = document.getElementById("newName").value;
        }
    }
    var newDescription = null;
    if(document.getElementById("newDescription").value !== null) {
        console.log("WTF");
        if(document.getElementById("newDescription").value === eventName) {
            newDescription = eventDescription
        } else {
            newDescription = document.getElementById("newDescription").value;
        }
    }
    var newStartingMonth = null;
    if(document.getElementById("newStartingMonth").value !== null) {
        if(document.getElementById("newStartingMonth").value === startingMonth) {
            newStartingMonth = startingMonth
        } else {
            newStartingMonth = document.getElementById("newStartingMonth").value;
        }
    }
    var newStartingDay = null;
    if(document.getElementById("newStartingDay").value !== null) {
        if(document.getElementById("newStartingDay").value === startingDay) {
            newStartingDay = startingDay
        } else {
            newStartingDay = document.getElementById("newStartingDay").value;
        }
    }
    var newStartingYear = null;
    if(document.getElementById("newStartingYear").value !== null) {
        if(document.getElementById("newStartingYear").value === startingYear) {
            newStartingYear = startingYear
        } else {
            newStartingYear = document.getElementById("newStartingYear").value;
        }
    }
    var newDueMonth = null;
    if(document.getElementById("newDueMonth").value !== null) {
        if(document.getElementById("newDueMonth").value === dueMonth) {
            newDueMonth = dueMonth
        } else {
            newDueMonth = document.getElementById("newDueMonth").value;
        }
    }
    var newDueDay = null;
    if(document.getElementById("newDueDay").value !== null) {
        if(document.getElementById("newDueDay").value === dueDay) {
            newDueDay = dueDay
        } else {
            newDueDay = document.getElementById("newDueDay").value;
        }
    }
    var newDueYear = null;
    if(document.getElementById("newDueYear").value !== null) {
        if(document.getElementById("newDueYear").value === dueYear) {
            newDueYear = dueYear
        } else {
            newDueYear = document.getElementById("newDueYear").value;
        }
    }
    var newDueHour = null;
    if(document.getElementById("newDueHour").value !== null) {
        if(document.getElementById("newDueHour").value === dueHour) {
            newDueHour = dueHour
        } else {
            newDueHour = document.getElementById("newDueHour").value;
        }
    }
    var newDueMinute = null;
    if(document.getElementById("newDueMinute").value !== null) {
        if (document.getElementById("newDueMinute") === dueMinute) {
            newDueMinute = dueMinute
        } else {
            newDueMinute = document.getElementById("newDueMinute").value;
        }
    }
    var newColor = document.getElementById("newColor").value.toString();
    console.log(newColor);
    var newColorString = newColor.substr(1);
    if(newColorString === eventColor) {
        newColorString = eventColor
    }
    var newPrivacyString;
    if(document.querySelector('input[name="privacyString"]:checked') !== null) {
        newPrivacyString= document.querySelector('input[name="privacyString"]:checked').value;
    } else {
        newPrivacyString=isPublic
    }
    if(newPrivacyString ===isPublic) {
        newPrivacyString = isPublic
    }

    $.ajax({
        url: 'api/event/user?eventId='+eventId+'&accessToken='+token+'&name='+newName+'&description='+newDescription+'&startingMonth='+newStartingMonth+'&startingDay='+newStartingDay+'&startingYear='+newStartingYear+'&dueMonth='+newDueMonth+'&dueDay='+newDueDay+'&dueYear='+newDueYear+'&dueHour='+newDueHour+'&dueMinute='+newDueMinute+'&color='+newColorString+'&privacyString='+newPrivacyString,
        method: "POST",

        success: function () {
            window.location.reload();
            getAllEvents();
            document.getElementById("eventList").style.display = "block";
        },
        error: function () {
            console.log("Event couldn't be edited.")
        }
    });

}