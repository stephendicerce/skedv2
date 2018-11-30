$.ajax({
    url: 'user/auth',
    method: 'GET',

    success: function (data) {
        token = data.data.token;
        applicationUser =  data.data.user;
        console.log(applicationUser);

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