package organizer

class UrlMappings {

    static mappings = {

        // Page url mapping
        "/"(controller: 'application', action: 'landing')
        "/dashboard"(controller: 'application', action: "dashboard")
        "/createEvent"(controller: 'application', action: "createEvent")
        "/createOrganization"(controller: 'application', action: "createOrganization")
        "/myEvents"(controller: 'application', action: "eventList")
        "/myOrganizations"(controller: 'application', action: "orgList")
        "/findFriends"(controller: 'application', action: "findFriends")
        "/following"(controller: 'application', action: "following")
        "/help"(controller: 'application',action: 'help')


        "/user/auth"(controller: 'auth', action: 'auth', method: 'post')
        "/user/auth"(controller: 'auth', action: 'current', method: 'get')
        "/user/logout"(controller: 'auth', action: 'logout', method: 'post')

        group "/api/user", {
            "/"(controller: 'user', action: 'getUser', method: 'get')
            "/follow"(controller: 'user', action: 'followUser', method:'post')
            "/following"(controller: 'user', action: 'getFollowing', method: 'get')
            "/unfollow"(controller: 'user', action: 'removeUserFromFollowing', method: 'post')
        }

        group "/api/organization", {
            "/"(controller: 'organization', action: 'putOrganization', method: 'put')
            "/"(controller: 'organization', action: 'organizationGet', method: 'get')
            "/"(controller: 'organization', action: 'deleteOrganization', method: 'delete')

            "/users"(controller: 'organization', action: 'getOrganizationUsers', method: 'get')
            "/user/all"(controller: 'organization', action: 'getAllOrganizations', method: 'get')
            "/user/add"(controller: 'organization', action: 'postUser', method: 'post')
            "/user/update"(controller: 'organization', action: 'updateUser', method: 'post')
        }

        group "/api/event", {
            //basic user functions
            "/user"(controller: 'event', action: 'getUserEvent', method: 'get')
            "/user"(controller: 'event', action: 'postUserEvent', method: 'post')
            "/user"(controller: 'event', action: 'putUserEvent', method: 'put')

            //more for user
            "/user/month"(controller: 'event', action: 'getUserEventsForMonth', method: 'get')
            "/user/all"(controller: 'event', action: 'getAllUserEvents', method: 'get')
            "/user/friend"(controller: 'event', action: 'getAllUserFriendEvents', method: 'get')

            //basic organization functions
            "/org"(controller: 'event', action: 'getOrgEvent', method: 'get')
            "/org"(controller: 'event', action: 'postOrgEvent', method: 'post')
            "/org"(controller: 'event', action: 'putOrgEvent', method: 'put')

            //more for organization
            "/org/month"(controller: 'event', action: 'getOrgEventsForMonth', method: 'get')

            //delete function
            "/"(controller: 'event', action: 'deleteEvent', method: 'delete')
        }

        group "/api/user", {
            "/"(controller: 'user', action: 'getUser', method: 'delete')
            "/follow"(controller: 'user', action: 'addUserToFollowList', method: 'post')
            "/unfollow"(controller: 'user', action: 'removeUserFromFollowList', method: 'post')
        }








        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
