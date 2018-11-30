var token = "";
var users = [];

function findUsers() {

    $.ajax({
        url: '/user/auth',
        method: 'GET',

        success: function (data) {
            token = data.data.token;

            var firstName = null;
            var lastName = null;

            if (document.getElementById("firstName") !== null) {
                firstName = document.getElementById("firstName").value;
            }

            if (document.getElementById("lastName")!== null) {
                lastName = document.getElementById("lastName").value;
            }

            var urlString = '/api/user?accessToken='+token;
            if (firstName !== null) {
                urlString += '&first_name='+firstName;
            }
            if(lastName !== null) {
                urlString += '&last_name='+lastName;
            }

            console.log(urlString);

            $.ajax({
                url: urlString,
                method: 'GET',

                success: function (data) {
                    users = data.data.users;
                    populateUsers();
                },
                error: function (data) {
                    alert(data.message);
                    window.location.reload()
                }
            })
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
    console.log("Users.length:" + users.length);

    var j = idName;
    console.log("J:" + j);
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
        '<br><button type="button" onclick="followUser('+userId+')">\n' +
        '<span class="sr-only">Toggle navigation</span> Follow User\'s Events <i class="fa fa-bars"></i>\n' +
        '</button>' +
        '</div></div>';
    return str;
}

function followUser(userId) {
    var urlString = 'api/user/follow?accessToken='+token+'&userId='+userId;

    $.ajax({
        url: urlString,
        method: 'POST',

        success: function () {
            window.location.href = '../following'
        },
        error: function (data) {
            window.location.reload();
            console.log("Following this user failed")
        }
    })

}


