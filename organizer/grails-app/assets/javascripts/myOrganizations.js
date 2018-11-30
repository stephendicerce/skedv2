var userOrgs = [];
var token = '';


$(function () {
   getAllOrganizations()
});

function getAllOrganizations() {
    $.ajax({
        url: '/user/auth',
        method: 'GET',

        success: function (data) {
            token = data.data.token;

            var urlString = 'api/organization/user/all?accessToken='+token;
            $.ajax({
                url: urlString,
                method: 'GET',

                success: function (data) {
                    userOrgs = data.data.organizations;

                    var i;
                    var orgsDiv = document.getElementById("orgList");
                    if(userOrgs.length === 0) {
                        var string = "<p style=\"text-align: center;\">You currently don't belong to any organizations.</p>";
                        var div = document.createElement("div");
                        div.innerHTML = string;
                        orgsDiv.appendChild(div);
                    }

                    for (i = 0; i < userOrgs.length; i++) {
                        var description;
                        if(userOrgs[i].orgDescription === null){
                            description = "This organization has no description"
                        } else {
                            description = userOrgs[i].orgDescription
                        }
                        console.log(userOrgs[i].orgId);
                        var htmlString = orgHTML(userOrgs[i].orgName, description, userOrgs[i].members, userOrgs[i].orgId);
                        var newDiv = document.createElement("div");
                        newDiv.innerHTML = htmlString;
                        orgsDiv.appendChild(newDiv);
                    }
                }
            });
        }
    });
}


function orgHTML(orgName, description, members, orgId) {
    //var str = '<div class="col-md-4 col-sm-6 portfolio-item" style="box-shadow: 0px 0px 0px gray; padding: 20px;">'
    var str = '<div id="org'+orgId+'" class="col-md-4 col-sm-6 portfolio-item" style="box-shadow: 5px 5px 30px gray; padding: 5px;">';
    //var str = '<div class="col-md-4 col-sm-6 portfolio-item" style="box-shadow: 0px 0px 0px gray; padding: 10px;">'
    str += '<div class="portfolio-hover">';
    str += '<div class="portfolio-hover-content">';
    str += '<i class="fa fa-plus fa-3x"></i></div></div>';
    //str += '<asset:image class="img-responsive" src="logo.png" alt=""/>'
    //str += '<img src="assets/images/startup-framework.png" class="img-responsive" alt=""></a>'
    str += '<div class="portfolio-caption"><h4>' +orgName +'</h4><p class="text-muted"> Description: '+ description + '<br>Number of Members: '+members+'</p>' +
        '<button type="button" onclick="editOrg('+orgId+')">\n' +
        '<span class="sr-only">Toggle navigation</span> Edit Info <i class="fa fa-bars"></i>\n' +
        '</button>&nbsp;' +
        '<button type="button" onclick="addUser('+orgId+')">\n' +
        '<span class="sr-only">Toggle navigation</span> Add User to Organization <i class="fa fa-bars"></i>\n' +
        '</button><br>' +
        '<button type="button" onclick="viewUsersInOrg('+orgId+')">' +
        '<span class="sr-only">ToggleNavigation</span> View Users In Organization<i class="fa fa-bars"></i> ' +
        '</button>&nbsp;' +
        '<button type="button" onclick="deleteOrg('+orgId+')">' +
        '<span class="sr-only">Toggle navigation</span> Delete Organization <i class="fa fa-bars"></i>' +
        '</button></div></div>';

    return str
}

function deleteOrg(orgId) {
    console.log(orgId);
    $.ajax({
        url: '/user/auth',
        method: "GET",

        success: function (data) {
            token = data.data.token;
            console.log(token);
            $.ajax({
                url: 'api/organization?accessToken=' + token + '&organizationId=' + orgId,
                method: "DELETE",
                success: function () {
                    window.location.reload()
                },
                error: function () {
                    alert("Organization could not be deleted, please try again");
                    getAllEvents()
                }
            });
        }
    });
}

function addUser(orgId) {
    console.log("OrgId: "+ orgId);
    var addUserDiv = document.getElementById("addUserDiv");
    document.getElementById("orgList").style.display = "none";
    var addUserString = '<div id="userSearchDiv">';
    addUserString += 'Find a user to add by searching for the user by name.<br><br>';
    addUserString += 'First Name:<br>';
    addUserString += '<input type="text" id="userFirstName"><br><br>';
    addUserString += 'Last Name:<br>';
    addUserString += '<input type="text" id="userLastName"><br><br>';
    addUserString += '<button type="button" onclick=findUsers('+orgId+')>';
    addUserString += '<span class="sr-only">Toggle navigation</span> Find Users<i class="fa fa-bars"></i>';
    addUserString += '</button></div>';

    var newDiv = document.createElement("div");
    newDiv.innerHTML = addUserString;
    addUserDiv.appendChild(newDiv);

}

var users = [];
function findUsers(orgId) {
console.log("findUsers OrgId: "+orgId);
    $.ajax({
        url: '/user/auth',
        method: 'GET',

        success: function (data) {
            token = data.data.token;

            var firstName = null;
            var lastName = null;

            if (document.getElementById("userFirstName") !== null) {
                firstName = document.getElementById("userFirstName").value;
            }

            if (document.getElementById("userLastName")!== null) {
                lastName = document.getElementById("userLastName").value;
            }

            var urlString = '/api/user?accessToken='+token;
            if (firstName !== null) {
                urlString += '&first_name='+firstName;
            }
            if(lastName !== null) {
                urlString += '&last_name='+lastName;
            }

            $.ajax({
                url: urlString,
                method: 'GET',

                success: function (data) {
                    users = data.data.users;
                    populateUsers(orgId);
                },
                error: function (data) {
                    alert(data.message);
                    window.location.reload()
                }
            })
        }
    });
}

function populateUsers(orgId) {
    console.log("populateUsers orgId: "+orgId);
    var i;
    var usersDiv = document.getElementById("userList");
    if(users.length === 0) {
        var string = "<p style=\"text-align: center;\">No users were found with the information you entered.</p>";
        var div = document.createElement("div");
        div.innerHTML = string;
        usersDiv.appendChild(div);
    }

    var j = idName;
    for(j; j>0;j--){
        document.getElementById("user"+j).style.display = "none"
    }
    for (i = 0; i < users.length; i++) {
        var userName;
        if(users[i].userName === null){
            userName = "This user has no name"
        } else {
            userName = users[i].userName
        }
        var userPicture;
        if(users[i].userImageUrl === null) {
            userPicture = "no picture"
        } else {
            userPicture = users[i].userImageUrl
        }

        var htmlString = usersHTML(userName, users[i].email, userPicture, users[i].userId, orgId);
        var newDiv = document.createElement("div");
        newDiv.innerHTML = htmlString;
        usersDiv.appendChild(newDiv);
    }

}

var idName = 0;
function usersHTML(userName, email, userPicture, userId, orgId) {
    console.log("usersHTML orgId: "+ orgId);
    idName++;

    var str = '<div class="userEvent col-md-4 col-sm-6 portfolio-item" id="user'+idName+'" style="border:black solid">';
    str += '<div class="portfolio-hover">';
    str += '<div class="portfolio-hover-content">';
    str += '<i class="fa fa-plus fa-3x"></i></div></div><br>';
    str += '<img src="'+userPicture+'" class="img-circle" style="width: 15%">';
    str += '<div class="portfolio-caption"><h4>' +userName +'</h4><p class="text-muted"> Email: '+ email +
        '<br><button type="button" onclick="addUserToOrg('+userId+','+orgId+')">\n' +
        '<span class="sr-only">Toggle navigation</span> Add User to Organization<i class="fa fa-bars"></i>\n' +
        '</button>' +
        '</div></div>';
    return str;
}

var orgUsers = [];

function addUserToOrg(userId, orgId) {
    console.log("AddUserToOrg orgID: " +orgId);

    var urlString = 'api/organization/user/add?accessToken='+token+'&userId='+userId+'&orgId='+orgId;
    $.ajax({
        url: urlString,
        method: 'POST',

        success: function (data) {
            orgUsers = data.data.user;
            viewUsersInOrg(orgId)
        },
        error: function () {
            console.log("Adding User Failed")
        }
    });

}

function viewUsersInOrg(orgId) {
    console.log("ViewUsersInOrg OrgId: "+orgId);
    document.getElementById("orgList").style.display = "none";

    var organization;

    $.ajax({
       url: 'api/organization?accessToken='+token+'&organizationId='+orgId,
       method: 'GET',

       success: function (data) {
           organization = data.data.userOrg;
           document.getElementById("userList").style.display = "none";
           document.getElementById("addUserDiv").style.display = "none";
           var orgAdded = document.getElementById("orgAdded");

           var description = organization.orgDescription;

           var htmlString = orgHTML(organization.orgName, description, organization.members, orgId);
           var newDiv = document.createElement("div");
           newDiv.innerHTML = htmlString;
           orgAdded.appendChild(newDiv);

           $.ajax({
              url: 'api/organization/users?accessToken='+token+'&orgId='+orgId,
              method: "GET",

              success: function (data) {
                  orgUsers = data.data.users;
                  console.log(data.data.token);
                  populateOrgUsers(orgUsers, orgId);
              },
               error: function () {
                   console.log("GETTING ORG USERS FAILED")
               }
           });
       },
        error: function () {
            console.log("retrieving org failed.")
        }
    });
}

function populateOrgUsers(orgUsers, orgId) {
    var i;
    var orgUsersDiv = document.getElementById("orgUsers");
    if(orgUsers.length === 0) {
        var string = "<p style=\"text-align: center;\">No users were found with the information you entered.</p>";
        var div = document.createElement("div");
        div.innerHTML = string;
        orgUsersDiv.appendChild(div);
    }
    console.log("orgUsers.length:" + orgUsers.length);

    for (i = 0; i < orgUsers.length; i++) {
        var userName;
        if(orgUsers[i].userName === null){
            userName = "This user has no name"
        } else {
            userName = orgUsers[i].userName
        }
        var userPicture;
        if(orgUsers[i].userImageUrl === null) {
            userPicture = "no picture"
        } else {
            userPicture = orgUsers[i].userImageUrl
        }

        var htmlString = orgUsersHTML(userName, orgUsers[i].email, userPicture, orgUsers[i].userId, orgId);
        var newDiv = document.createElement("div");
        newDiv.innerHTML = htmlString;
        orgUsersDiv.appendChild(newDiv);
    }
}

function orgUsersHTML(userName, email, userPicture, userId, orgId) {
    console.log("usersHTML orgId: "+ orgId);
    idName++;

    var str = '<div class="userEvent col-md-4 col-sm-6 portfolio-item" id="user'+idName+'" style="border:black solid">';
    str += '<div class="portfolio-hover">';
    str += '<div class="portfolio-hover-content">';
    str += '<i class="fa fa-plus fa-3x"></i></div></div><br>';
    str += '<img src="'+userPicture+'" class="img-circle" style="width: 15%">';
    str += '<div class="portfolio-caption"><h4>' +userName +'</h4><p class="text-muted"> Email: '+ email +
        '<br><button type="button" onclick="removeUserFromOrg('+userId+','+orgId+')">\n' +
        '<span class="sr-only">Toggle navigation</span> Remove User from Organization<i class="fa fa-bars"></i>\n' +
        '</button>' +
        '</div></div>';
    return str;
}

function removeUserFromOrg(userId, orgId) {

}