function openNav() {
    document.getElementById("mySidenav").style.width = "250px";
    if (document.location.pathname === "/") {
        //No user related data will be shown to potential users on the landing page
        document.getElementById("mySidenav").innerHTML = landingMenuContentHTML;
    } else if (document.location.pathname === "/dashboard") {
        //menu items will vary depending on which page the user is located on
        document.getElementById("mySidenav").innerHTML = dashboardMenuContentHTML;
    } else if (document.location.pathname === "/createEvent") {
        document.getElementById("mySidenav").innerHTML = createEventMenuContentHTML;
    } else if (document.location.pathname === "/createOrganization") {
        document.getElementById("mySidenav").innerHTML = createOrganizationMenuContentHTML;
    } else if (document.location.pathname === "/myEvents") {
        document.getElementById("mySidenav").innerHTML = myEventsMenuContentHTML;
    } else if (document.location.pathname === "/myOrganizations") {
    document.getElementById("mySidenav").innerHTML = myOrganizationsMenuContentHTML;
    } else if (document.location.pathname === "/findFriends") {
        document.getElementById("mySidenav").innerHTML = findFriendsMenuContentHTML;
    }else if (document.location.pathname === "/help") {
        document.getElementById("mySidenav").innerHTML = helpMenuContentHTML;
    }else if (document.location.pathname === "/following") {
    document.getElementById("mySidenav").innerHTML = followingMenuContentHTML;
}
}

function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
}

var landingMenuContentHTML = '<a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times </a>' +
    '<a href="../help">Help Page</a> ' +
    '<a href="../about">About Us</a> ' +
    '<a href="../faqs">FAQs</a>';

var dashboardMenuContentHTML = '<a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times </a>' +
    '<a href="../following">My Friends</a>' +
    '<a href="../findFriends">Find Friends</a>' +
    '<a href="../myEvents">My Events</a>' +
    '<a href="../createEvent">Create Event</a>' +
    '<a href="../myOrganizations">My Organizations</a>\n' +
    '<a href="../createOrganization">Create An Organization</a>' +
    '<a href="../help">Help</a>';

var createEventMenuContentHTML = '<a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times </a>' +
    '<a href="../dashboard">Dashboard</a>' +
    '<a href="../following">My Friends</a>' +
    '<a href="../findFriends">Find Friends</a>' +
    '<a href="../myEvents">My Events</a>' +
    '<a href="../myOrganizations">My Organizations</a>\n' +
    '<a href="../createOrganization">Create An Organization</a>' +
    '<a href="../help">Help</a>';

var createOrganizationMenuContentHTML = '<a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times </a>' +
    '<a href="../dashboard">Dashboard</a>' +
    '<a href="../following">My Friends</a>' +
    '<a href="../findFriends">Find Friends</a>' +
    '<a href="../myEvents">My Events</a>' +
    '<a href="../createEvent">Create Event</a>' +
    '<a href="../myOrganizations">My Organizations</a>\n' +
    '<a href="../help">Help</a>';

var myEventsMenuContentHTML = '<a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times </a>' +
    '<a href="../dashboard">Dashboard</a>' +
    '<a href="../following">My Friends</a>' +
    '<a href="../findFriends">Find Friends</a>' +
    '<a href="../createEvent">Create Event</a>' +
    '<a href="../myOrganizations">My Organizations</a>\n' +
    '<a href="../createOrganization"> Create An Organization</a>' +
    '<a href="../help">Help</a>';

var myOrganizationsMenuContentHTML = '<a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times </a>' +
    '<a href="../dashboard">Dashboard</a>' +
    '<a href="../following">My Friends</a>' +
    '<a href="../findFriends">Find Friends</a>' +
    '<a href="../myEvents">My Events</a>' +
    '<a href="../createEvent">Create Event</a>' +
    '<a href="../createOrganization"> Create An Organization</a>' +
    '<a href="../help">Help</a>';

var findFriendsMenuContentHTML = '<a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times </a>' +
    '<a href="../dashboard">Dashboard</a>' +
    '<a href="../following">My Friends</a>' +
    '<a href="../myEvents">My Events</a>' +
    '<a href="../createEvent">Create Event</a>' +
    '<a href="../myOrganizations">My Organizations</a>' +
    '<a href="../createOrganization"> Create An Organization</a>' +
    '<a href="../help">Help</a>';

var followingMenuContentHTML = '<a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times </a>' +
    '<a href="../dashboard">Dashboard</a>' +
    '<a href="../findFriends">Find Friends</a>' +
    '<a href="../myEvents">My Events</a>' +
    '<a href="../createEvent">Create Event</a>' +
    '<a href="../myOrganizations">My Organizations</a>\n' +
    '<a href="../createOrganization">Create An Organization</a>' +
    '<a href="../help">Help</a>';

var helpMenuContentHTML = '<a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times </a>' +
    '<a href="../">Landing</a> ' +
    '<a href="../dashboard">Dashboard</a> ' +
    '<a href="../about">About Us</a> ' +
    '<a href="../faqs">FAQs</a>';