package organizer

import util.QueryResult

class EventController {

    static responseFormats = ['json', 'xml']

    PreconditionService preconditionService
    EventService eventService

    def getUserEvent(String accessToken, String eventId) {
        def require = preconditionService.notNull(params, ["accessToken"])
        def token = preconditionService.accessToken(accessToken, require).data
        println "got require"
        if(require.success) {
            println "getting result"
            def result = eventService.getUserEvent(token, eventId)
            println "got result"
            if(result.success) {
                println "event id :" + result.data.id

                render(view: 'userEvent', model: [token: token, event: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }

        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    def putUserEvent(String accessToken, String name, String description, String startingMonth, String startingDay, String startingYear, String dueMonth, String dueDay, String dueYear, String dueMinute, String dueHour, String color, String privacyString) {
        def require = preconditionService.notNull(params, ["accessToken", "name", "dueMonth", "dueDay", "dueYear", "privacyString"])
        def token = preconditionService.accessToken(accessToken, require).data

        if(require.success) {
            def result = eventService.createEvent(token, null, name, description, startingMonth, startingDay, startingYear, dueMonth, dueDay, dueYear, dueMinute, dueHour, color, privacyString)

            if(result.success) {
                render(view: 'userEvent', model: [token: token, event: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    def putOrgEvent(String accessToken, String orgId, String name, String description, String startingMonth, String startingDay, String startingYear, String dueMonth, String dueDay, String dueYear, String dueMinute, String dueHour, String color) {
        def require = preconditionService.notNull(params, ["accessToken", "orgId", "name", "dueMonth", "dueDay", "dueYear"])
        def token = preconditionService.accessToken(accessToken, require).data

        if(require.success) {
            def result = eventService.createEvent(token, orgId, name, description, startingMonth, startingDay, startingYear, dueMonth, dueDay, dueYear, dueMinute, dueHour, color, "true")

            if(result.success) {
                render(view: 'orgEvent', model: [token: token, event: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    def postUserEvent(String eventId, String accessToken, String name, String description, String startingMonth, String startingDay, String startingYear, String dueMonth, String dueDay, String dueYear, String dueMinute, String dueHour, String color, String privacyString) {
        def require = preconditionService.notNull(params, ["accessToken", "eventId"])
        def token = preconditionService.accessToken(accessToken, require).data
        println "got require"
        if(require.success) {
            println "getting result"
            def result = eventService.editEvent(eventId, token, null, name, description, startingMonth, startingDay, startingYear, dueMonth, dueDay, dueYear, dueMinute, dueHour, color, privacyString)
            println "got result"
            if(result.success) {
                println "Trying to render"
                render(view: 'userEvent', model: [token: token, event: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    def postOrgEvent(String eventId, String accessToken, String orgId, String name, String description, String startingMonth, String startingDay, String startingYear, String dueMonth, String dueDay, String dueYear, String dueMinute, String dueHour, String color) {
        def require = preconditionService.notNull(params, ["accessToken", "orgId", "eventId"])
        def token = preconditionService.accessToken(accessToken, require).data

        if(require.success) {
            def result = eventService.editEvent(eventId, token, orgId, name, description, startingMonth, startingDay, startingYear, dueMonth, dueDay, dueYear, dueMinute, dueHour, color, "true")

            if(result.success) {
                render(view: 'orgEvent', model: [token: token, event: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    def deleteEvent(String accessToken, String eventId) {
        def require = preconditionService.notNull(params, ["accessToken", "eventId"])
        def token = preconditionService.accessToken(accessToken, require).data

        if(require.success) {
            def result = eventService.deleteEvent(token, eventId)

            if(result.success) {
                render(view: "deleteEvent", model: [token: token])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    def getUserEventsForMonth(String accessToken, String monthString) {
        def require = preconditionService.notNull(params, ["accessToken", "monthString"])
        def token = preconditionService.accessToken(accessToken, require).data

        if(require.success) {
            def result = eventService.getAllUserEventsByMonth(token, monthString)

            if(result.success) {
                render(view: "userEventsList", model: [token: token, events: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    def getOrgEventsForMonth(String accessToken, String orgId, String monthString) {
        def require = preconditionService.notNull(params, ["accessToken", "orgId", "monthString"])
        def token = preconditionService.accessToken(accessToken, require).data

        if(require.success) {
            def result = eventService.getAllOrganizationEventsByMonth(token, orgId, monthString)

            if(result.success) {
                render(view: "orgEventsList", model: [token: token, events: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    def getAllUserEvents(String accessToken) {
        def require = preconditionService.notNull(params, ["accessToken"])
        def token = preconditionService.accessToken(accessToken, require).data

        if(require.success) {
            def result = eventService.getAllEventsForRequestingUser(token, null, null)

            if(result.success) {
                render(view: "userEventsList", model: [token: token, events: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        }
    }

    def getAllUserFriendEvents(String accessToken, String friendId, String monthString) {
        def require = preconditionService.notNull(params, ["accessToken", "friendId"])
        def token = preconditionService.accessToken(accessToken, require).data

        if(require.success) {
            def result = eventService.getAllEventsForRequestingUser(token, friendId, monthString)

            if(result.success) {
                render(view: "userEventsList", model: [token: token, events: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }
}
