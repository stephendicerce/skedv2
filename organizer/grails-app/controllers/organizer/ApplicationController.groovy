package organizer

import util.QueryResult

import javax.management.Query

class ApplicationController {
    PreconditionService preconditionService

    def landing() {
        render(view: 'landing')
    }

    def dashboard() {
        //println "Doing dashboard stuff"
        QueryResult<AuthToken> access = hasAccess()
        if(access.success) {
            User user = access.data.user
            render(view: 'dashboard')
        } else {
            println "User isn't logged in"
            session.invalidate()
            render(view: 'landing')
        }
    }

    def createEvent() {
        QueryResult<AuthToken> access = hasAccess()
        if(access.success) {
            User user = access.data.user
            render(view: 'createEvent')
        } else {
            println "User isn't logged in"
            session.invalidate()
            render(view: 'landing')
        }
    }

    def createOrganization() {
        QueryResult<AuthToken> access = hasAccess()
        if(access.success) {
            User user = access.data.user
            render(view: "createOrganization")
        } else {
            println "User isn't logged in"
            session.invalidate()
            render(view: "landing")
        }
    }

    def eventList() {
        QueryResult<AuthToken> access = hasAccess()
        if(access.success) {
            User user = access.data.user
            render(view: 'myEvents')
        } else {
            println "User isn't logged in"
            session.invalidate()
            render(view: "landing")
        }
    }

    def orgList() {
        QueryResult<AuthToken> access = hasAccess()
        if(access.success) {
            User user = access.data.user
            render(view: 'myOrganizations')
        } else {
            session.invalidate()
            render(view: 'landing')
        }
    }

    def findFriends() {
        QueryResult<AuthToken> access = hasAccess()
        if(access.success) {
            User user = access.data.user
            render(view: 'findFriends')
        } else {
            session.invalidate()
            render(view: 'landing')
        }
    }

    def following() {
        QueryResult<AuthToken> access = hasAccess()
        if(access.success) {
            User user = access.data.user
            render(view: 'following')
        } else {
            session.invalidate()
            render(view: 'landing')
        }
    }

    def help() {
        render(view: 'help')
    }

    private QueryResult<AuthToken> hasAccess() {
        String access = session.getAttribute("access")
        preconditionService.accessToken(access)
    }
}
