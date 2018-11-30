var users = [];
var token = '';
var nameID = 0;

$(function () {
    getAllFriends()
});


function getAllFriends() {

    $.ajax({
        url: '/user/auth',
        method: 'GET',

        success: function (data) {
            token = data.data.token;

            var urlString = '/api/user/following?accessToken='+token;

            $.ajax({
                url: urlString,
                method: 'GET',

                success: function (data) {
                    users = data.data.users;

                    populateUsers()
                },
                error: function () {
                    console.log("error getting users that are followed")
                }
            });
        },
        error: function () {
            console.log("error getting user")
        }
    });
}

function populateUsers() {
    var i;
    var usersDiv = document.getElementById("userList");
    if(users.length === 0) {
        var string = "<p style=\"text-align: center;\">No users were found with the information you entered.</p>";
        var div = document.createElement("div");
        div.innerHTML = string;
        usersDiv.appendChild(div);
    }
    console.log("Users Length: " + users.length);
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

        console.log('User ID: '+users[i].userId);
        var htmlString = usersHTML(userName, users[i].email, userPicture, users[i].userId);
        var newDiv = document.createElement("div");
        newDiv.innerHTML = htmlString;
        usersDiv.appendChild(newDiv);
    }

}

var idName = 0;
function usersHTML(userName, email, userPicture, userId) {
    idName++;

    var str = '<div class="userEvent col-md-4 col-sm-6 portfolio-item" id="user'+idName+'" style="border:black solid">';
    console.log(idName);
    str += '<div class="portfolio-hover">';
    str += '<div class="portfolio-hover-content">';
    str += '<i class="fa fa-plus fa-3x"></i></div></div><br>';
    str += '<img src="'+userPicture+'" class="img-circle" style="width: 15%">';
    str += '<div class="portfolio-caption"><h4>' +userName +'</h4><p class="text-muted"> Email: '+ email +
        '<br><button type="button" onclick="unfollowUser('+userId+')">\n' +
        '<span class="sr-only">Toggle navigation</span> Unfollow <i class="fa fa-bars"></i>\n' +
        '</button>' +
        '</div></div>';
    return str;
}

function unfollowUser(userId) {

    $.ajax({
        url: '/api/user/unfollow?accessToken='+token+'&followerId='+userId,
        method: 'POST',
        success: function (data) {
            var users = data.data;
            alert("You have successfully unfollowed the user.");
            window.location.reload()
        },
        error: function () {
            window.location.reload()
        }
    });

}