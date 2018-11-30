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

function sendOrg() {
    var name = document.getElementById("name").value;
    var orgDescription = document.getElementById("orgDescription").value;
    var url = 'api/organization?accessToken='+token+'&name='+name+'&description='+orgDescription;
    
    $.ajax({
        url:url,
        type: 'PUT',
        success: function () {
            window.location.href = '../myOrganizations'
        },
        error: function () {
            alert("Organization Creation was unsuccessful, please try again.");
            window.location.href = '../createOrganization'
        }
    });
}