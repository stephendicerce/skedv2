var token = '';

$(function() {
    $.ajax({
        url: '/user/auth',
        method: "GET",
        success: function (data) {
            token = data.data.token;
        }
    });
});


function organizationDropdown() {
    listOrgs()
    document.getElementById("myDropdown").classList.toggle("show");
}

// Close the dropdown if the user clicks outside of it
window.onclick = function(event) {
    if (!event.target.matches('.dropbtn')) {

        var dropdowns = document.getElementsByClassName("dropdown-content");
        var i;
        for (i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
};

function toggleOrgDropdown() {
    var dropdown = document.getElementById('orgDropdown');
    var privacy = document.getElementById('privacyRadio');
    var button = document.getElementById('organizationEventButton');
    if(dropdown.style.display === "block") {
        dropdown.style.display = "none";
        button.value = "false";
    } else {
        dropdown.style.display = "block";
        button.value = "true";
    }

    if(privacy.style.display === "none"){
        privacy.style.display = "block";
    } else {
        privacy.style.display = "none"
    }
}

function sendEvent() {
    var name = document.getElementById("name").value;
    var description = document.getElementById("eventDescription").value;
    var startingMonth = document.getElementById("startingMonth").value;
    var startingDay = document.getElementById("startingDay").value;
    var startingYear = document.getElementById("startingYear").value;
    var dueHour = document.getElementById("dueHour").value;
    var dueMinute = document.getElementById("dueMinute").value;
    var dueMonth = document.getElementById("dueMonth").value;
    var dueDay = document.getElementById("dueDay").value;
    var dueYear = document.getElementById("dueYear").value;
    var color = document.getElementById("color").value.toString();
    var colorString = color.substr(1);
    console.log(colorString);
    var privacyString = document.querySelector('input[name="privacyString"]:checked').value;
    var orgId;
    sendUserEvent(name, description, startingMonth, startingDay, startingYear, dueMonth, dueDay, dueYear, dueMinute, dueHour, colorString, privacyString)



}

function getOrganizationId() {
    
}



function listOrgs() {
    var orgList = document.getElementById("orgList");
    for(i; i<5; i++) {
        var string = '<div id="org'+i+'">Org'+i+'</div>'
        var div = document.createElement("div")
        div.innerHTML = string
        orgList.appendChild(div)
    }
}

function sendUserEvent(name, description, startingMonth, startingDay, startingYear, dueMonth, dueDay, dueYear, dueMinute, dueHour, color, privacyString) {
    var url = '/api/event/user?accessToken='+token+'&name='+name+'&dueMonth='+dueMonth+'&dueDay='+dueDay+'&dueYear='+dueYear+'&privacyString='+privacyString;
    if(description !== ''){
        url += '&description='+description
    }
    if(startingMonth!==''){
        url += '&startingMonth='+startingMonth
    }
    if(startingDay !=='') {
        url += '&startingDay='+startingDay
    }
    if(startingYear !=='') {
        url += '&startingYear='+startingYear
    }
    if(dueMinute !=='') {
        url += '&dueMinute='+dueMinute
    }
    if(dueHour!=='') {
        url += '&dueHour='+dueHour
    }
    if(color!=='') {
        url +='&color='+color
    }
    console.log(url);
    $.ajax({
        url: url,
        type: 'PUT',
        success: function (data) {
            window.location.href = "../dashboard"
        },
        error: function (jqXHR, textStatus, errorMessage) {
            window.location.href = "../createEvent";
            console.log(errorMessage)
            alert(errorMessage + " Event creation was Unsuccessful, please try again.")
        }
    });
}

function sendOrgEvent(orgId, name, description, startingMonth, startingDay, startingYear, dueMonth, dueDay, dueYear, dueMinute, dueHour, color) {
    $.ajax({
        url: '../api/event/org?token=' + token + '&orgId=' + orgId + '&name=' + name + '&description=' + description + '&startingMonth=' + startingMonth + '&startingDay=' + startingDay + '&startingYear=' + startingYear + '&dueMonth=' + dueMonth + '&dueDay=' + dueDay + '&dueYear=' + dueYear + '&dueMinute=' + dueMinute + '&dueHour=' + dueHour + '&color=' + color + '&privacyString=true',
        type: 'PUT',
        success: function (data) {
            window.location.href = "../dashboard"
        },
        error: function (jqXHR, textStatus, errorMessage) {
            console.log(errorMessage)
        }
    });
}