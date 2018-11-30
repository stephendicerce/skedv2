package organizer

import grails.gorm.transactions.Transactional
import org.springframework.boot.actuate.autoconfigure.ShellProperties.Auth
import org.springframework.http.HttpStatus
import util.QueryResult

@Transactional
class EventService {

    /**
     * Method to create an userEvent
     * @param token the access token of the requesting user
     * @param orgId (Optional) the organization the userEvent will belong to
     * @param name The name of the userEvent
     * @param description (Optional) a short description of the userEvent
     * @param startingMonth (Optional) A string representing the number of the month when the userEvent will begin
     * @param startingDay (Optional) A string representing the number of the day when the userEvent will begin
     * @param startingYear (Optional) A string representing the year when the userEvent will begin
     * @param dueMonth A string representing the number of the month when the userEvent will end
     * @param dueDay A string representing the number of the day when the userEvent will end
     * @param dueYear A string representing the number of the year when the userEvent will end
     * @param dueMinute (Optional) A string representing the minute when the userEvent is due
     * @param dueHour (Optional) A string representing the hour when the userEvent is due
     * @param color (Optional) A string representing the color the userEvent will be displayed in on the User's Calendar
     * @param res A QueryResult stores data from the method
     * @param privacyString A boolean representing whether the userEvent will be private or public
     * @return A QueryResult The result of the method
     */
    QueryResult<Event> createEvent(AuthToken token, String orgId, String name, String description, String startingMonth, String startingDay, String startingYear, String dueMonth, String dueDay, String dueYear, String dueMinute, String dueHour, String color, String privacyString) {
        println "In create Event"
        QueryResult<Event> res = new QueryResult<>(success: true)
        int monthDue = dueMonth.isInteger() ? dueMonth.toInteger() : -1
        int dayDue = dueDay.isInteger() ? dueDay.toInteger() : -1
        int yearDue = dueYear.isInteger() ? dueYear.toInteger() : -1
        int isPublic = 1


        if(privacyString != null) {
            if (privacyString.equalsIgnoreCase("true"))
                isPublic = 0
        }

        Organization organization

        User user = token?.user
        if(user != null) {
            Event event = new Event(name: name, dueMonth: monthDue, dueDay: dayDue, dueYear: yearDue)

            event.isPublic = isPublic
            if (description != null)
                event.description = description
            if (startingMonth != null)
                event.startingMonth = startingMonth.isInteger() ? startingMonth.toInteger() : -1
            if (startingYear != null)
                event.startingYear = startingYear.isInteger() ? startingYear.toInteger() : -1
            if (startingDay != null)
                event.startingDay = startingDay.isInteger() ? startingDay.toInteger() : -1
            if (dueHour != null)
                event.dueHour = dueHour.isInteger() ? dueHour.toInteger() : -1
            if (dueMinute != null)
                event.dueMinute = dueMinute.isInteger() ? dueMinute.toInteger() : -1
            if (color != null)
                event.color = color

            if (orgId != null) {
                Long organizationId = orgId.isLong() ? orgId.toLong() : -1
                organization = Organization.findById(organizationId)
                if (user.id == organization.orgOwner.id)
                    res = createOrganizationEvent(organization, event, res)
                else
                    QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, res)
            } else {
                println "createUserEvent"
                res = createUserEvent(user, event, res)
            }
        } else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, res)
        }
        res
    }

    /**
     * A Method that creates an userEvent that is owned by an organization
     * @param organization The organization that owns the userEvent
     * @param event The userEvent in the process of being created
     * @param result A QueryResult to store the data
     * @return the result of the userEvent being created
     */
    QueryResult<Event> createOrganizationEvent(Organization organization, Event event, QueryResult<Event> result) {
        event.organization = organization
        event.save(flush: true, failOnError: true)
        result.data = event
        result
    }

    /**
     * A method to create an userEvent that is owned by a user
     * @param user The user that will own the userEvent
     * @param event The userEvent in the process of being created
     * @param result A QueryResult to store data
     * @return The result of the userEvent being created
     */
    QueryResult<Event> createUserEvent(User user, Event event, QueryResult<Event> result) {
        event.user = user
        event.save(flush: true, failOnError: true)
        result.data = event
        result
    }

    /**
     * A method to get the userEvent
     * @param token The access token of the requesting user
     * @param eventId The id of the userEvent that the user is requesting
     * @param result A QueryResult that can store data
     * @return The userEvent
     */
    QueryResult<Event> getUserEvent(AuthToken token, String eventId) {
        QueryResult<Event> result = new QueryResult<>(success: true)
        User requestingUser = token?.user
        Long eID = eventId.isLong() ? eventId.toLong() : -1

        if(requestingUser != null) {
            if(eID != -1) {
                Event event = Event.findById(eID)
                if (event.user != null) {
                    if (event.user.id == requestingUser.id)
                        result.data = event
                } else {
                    QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST,result)
                }
            } else {
                QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
            }
        } else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, result)
        }
        result
    }

    /**
     *
     * @param eventID
     * @param token
     * @param orgId
     * @param name
     * @param description
     * @param startingMonth
     * @param startingDay
     * @param startingYear
     * @param dueMonth
     * @param dueDay
     * @param dueYear
     * @param dueMinute
     * @param dueHour
     * @param color
     * @param res
     * @return
     */
    QueryResult<Event> editEvent(String eventID, AuthToken token, String orgId, String name, String description, String startingMonth, String startingDay, String startingYear, String dueMonth, String dueDay, String dueYear, String dueMinute, String dueHour, String color, String privacyString) {
        QueryResult<Event> res = new QueryResult<>(success: true)
        User requestingUser = token?.user
        Long eID = eventID.isLong() ? eventID.toLong() : -1
        Event event = Event.findById(eID)
        int startMonth = event.startingMonth, startDay = event.startingDay, startYear = event.startingYear, monthDue = event.dueMonth
        int dayDue = event.dueDay, yearDue = event.dueYear, minuteDue = event.dueMinute, hourDue = event.dueHour
        int isPublic

        if(event != null) {
            if(privacyString != null) {
                if (privacyString.equalsIgnoreCase("true"))
                    isPublic = 0
                else
                    isPublic = 1
            }
            if(startingMonth != null)
                startMonth = startingMonth.isInteger() ? startingMonth.toInteger() : event.startingMonth
            if(startingDay != null)
                startDay = startingDay.isInteger() ? startingMonth.toInteger() : event.startingDay
            if(startingYear != null)
                startYear = startingYear.isInteger() ? startingYear.toInteger() : event.startingYear
            if(dueMonth != null)
                monthDue = dueMonth.isInteger() ? dueMonth.toInteger() : event.dueMonth
            if(dueDay != null)
                dayDue = dueDay.isInteger() ? dueDay.toInteger() : event.dueDay
            if(dueYear != null)
                yearDue = dueYear.isInteger() ? dueYear.toInteger() : event.dueYear
            if(dueMinute != null)
                minuteDue = dueMinute.isInteger() ? dueMinute.toInteger() : event.dueMinute
            if(dueHour != null)
                hourDue = dueHour.isInteger() ? dueHour.toInteger() : event.dueHour

            if (requestingUser != null) {
                if(((event.organization != null) && (requestingUser.id == event.organization.orgOwner.id)) || ((event.organization==null) && (requestingUser.id == event.user.id))) {

                    //make updates based on the entered information
                    if(name != null) {
                        if (event.name != name)
                            event.name = name
                    }
                    if(description != null) {
                        if (event.description != description)
                            event.description = description
                    }
                    if (event.startingMonth != startMonth)
                        event.startingMonth = startMonth
                    if (event.startingDay != startingDay)
                        event.startingDay = startDay
                    if (event.startingYear != startYear)
                        event.startingYear = startYear
                    if (event.dueMonth != monthDue)
                        event.dueMonth = monthDue
                    if (event.dueDay != dayDue)
                        event.dueDay = dayDue
                    if (event.dueYear != yearDue)
                        event.dueYear = yearDue
                    if (event.dueMinute != minuteDue)
                        event.dueMinute = minuteDue
                    if (event.dueHour != hourDue)
                        event.dueHour = hourDue
                    if(color != null) {
                        if (event.color != color)
                            event.color = color
                    }
                    if(isPublic != null) {
                        if (event.isPublic != isPublic)
                            event.isPublic = isPublic
                    }

                    event.save(flush:true, failOnError: true)
                    res.data = event
                } else {
                    QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, res)
                }
            } else {
                QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, res)
            }

        } else {
            res = createEvent(token, orgId, name, description, startingMonth, startingDay, startingYear, dueMonth, dueDay, dueYear, dueMinute, dueHour, color, privacyString)
        }
        res
    }

    QueryResult<Event> deleteEvent(AuthToken token, String eventId) {
        User requestingUser = token?.user
        QueryResult<Event> result = new QueryResult<>(success: true)
        Long eID = eventId.isLong() ? eventId.toLong() : -1
        Event event = Event.findById(eID)
        if(requestingUser != null) {
            if (event != null) {
                if (event.user != null) { //user owned userEvent
                    if (event.user.id == requestingUser.id) {
                        event.delete(flush: true, failOnError: true)
                    } else {
                        QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, result)
                    }
                } else if(event.organization != null) { //organization owned userEvent
                    if(doesUserHaveReadWriteAccess(event.organization, requestingUser)) {
                        event.delete(flush: true, failOnError: true)
                    } else {
                        QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, result)
                    }
                }
            } else {
                QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
            }
        }
        else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, result)
        }
        result
    }

    /**
     * A method to check if the User is part of an organization
     * @param organization The organization in question
     * @param requestingUser The requesting user
     * @return true if the user is a part of the organization, false if the user is not part of the organization
     */
    boolean isInOrganization(Organization organization, User requestingUser) {
        for(User user: organization.users){
            if(user.id == requestingUser.id)
                return true
        }

        /*
        for(User admin: organization.admins) {
            if(admin.id == requestingUser.id)
                return true
        }
        */
        if(organization.orgOwner.id == requestingUser.id)
            return true
        return false
    }

    /**
     * A method to check if the user has read/write access in the organization
     * @param organization The organization in question
     * @param user The requesting user
     * @return true if the user has read/write access, false if the user does not have read write access
     */
    boolean doesUserHaveReadWriteAccess(Organization organization, User user) {
        /*
        for (User admin : organization.admins) {
            if(admin.id == user.id)
                true
        }
        */
        if(organization.orgOwner.id == user.id)
            true
        false
    }

    /**
     *
     * @param token
     * @param monthString
     * @return
     */
    QueryResult<List<Event>> getAllUserEventsByMonth(AuthToken token, String monthString) {
        QueryResult<List<Event>> result = new QueryResult<>(success: true)
        User requestingUser = token?.user
        if(requestingUser != null) {

            List<Event> userEvents = Event.findAllByUser(requestingUser)
            List<Event> monthEvents = new ArrayList<>()
            int month = 0

            if (monthString != null) {
                month = monthString.isInteger() ? monthString.toInteger() : -1
                for (event in userEvents) {
                    if (event.dueMonth == month)
                        monthEvents.add(event)
                }
                if(monthEvents.size() == 0)
                    result.message = "The user doesn't have any events stored."
                result.data = monthEvents
                result
            } else {
                QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
            }
        } else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, result)
        }
    }

    /**
     *
     * @param token
     * @param orgId
     * @param monthString
     * @return
     */
    QueryResult<List<Event>> getAllOrganizationEventsByMonth(AuthToken token, String orgId, String monthString) {
        QueryResult<List<Event>> result = new QueryResult<>(success: true)
        User requestingUser = token?.user
        Long oID = orgId.isLong() ? orgId.toLong() : -1

        if(requestingUser != null) {
            Organization organization = Organization.findById(oID)

            if(organization != null) {
               boolean userIsInOrganization = isInOrganization(organization, requestingUser)

                if(userIsInOrganization) {
                    List<Event> organizationEvents = Event.findAllByOrganization(organization)
                    List<Event> organizationEventsForMonth = new ArrayList<>()
                    int month = monthString.isInteger() ? monthString.toInteger() : -1

                    for(event in organizationEvents) {
                        if(event.dueMonth == month) {
                            organizationEventsForMonth.add(event)
                        }
                    }

                    if(organizationEventsForMonth.size() == 0)
                        result.message = "The organization doesn't have any stored events."
                    result.data = organizationEventsForMonth
                    result
                } else {
                    QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, result)
                }
            } else {
                QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
            }
        } else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED,result)
        }
    }

    /**
     *
     * @param token
     * @param userIdString
     * @return
     */
    QueryResult<List<Event>> getAllEventsForRequestingUser(AuthToken token, String userIdString, String monthString) {
        QueryResult<List<Event>> result = new QueryResult<>(success: true)
        List<Event>userFriendEvents
        List<Event>userFriendPublicEvents = new ArrayList<>()
        User user = token?.user
        Long userId
        int month = -1;
        if(monthString != null) {
            month = monthString.isInteger() ? monthString.toInteger() : -1
        }
        if(userIdString == null) {
            userId = -2
        } else {
            userId = userIdString.isLong() ? userIdString.toLong() : -1
        }
        if(user != null) {
            if(userId == -2) {
                result = getAllEventsForUser(token, user, result)
            } else if(userId > 0) {
                User requestedUserFriend
                for(friend in user.following) {
                    if (friend.id == userId) {
                        requestedUserFriend = friend
                    }
                }
                if(requestedUserFriend != null && (month>0 && month<13)) {
                    userFriendEvents = Event.findAllByUser(requestedUserFriend)
                    for (int i=0; i<userFriendEvents.size();++i) {
                        println "i: "+i
                        println "Size of event list: " + userFriendEvents.size()
                        if(userFriendEvents[i].isPublic == 0) {
                            println "ADDING"
                            userFriendPublicEvents.add(userFriendEvents[i])
                        }
                    }
                }

                println "PUBLIC EVENTS LENGTH: "+ userFriendPublicEvents.size()

                result.data = userFriendPublicEvents
            } else {
                QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
            }
        } else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, result)
        }
        result
    }

    /**
     *
     * @param token
     * @param user
     * @param result
     * @return
     */
    QueryResult<List<Event>> getAllEventsForUser(AuthToken token, User user, QueryResult<List<Event>> result) {
        User requestingUser = token?.user
        result = new QueryResult<>(success: true)

        if(requestingUser != null) {
            if(requestingUser.id == user.id) {
                println "User requesting their own event"
                result.data = Event.findAllByUser(user)
            } else {
                List<Event> userEvents = Event.findAllByUser(user)
                int count = 0
                for(event in userEvents){
                    if(event.isPublic) {
                        result.data.add(event)
                        ++count
                    }
                }
                if(count == 0) {
                    QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
                }
            }
            result
        } else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, result)
        }
    }
}















// below is frankenstein code

/*
QueryResult<List<Event>> getAllEventsForRequestingUser(AuthToken token, String userIdString, String monthString) {
        QueryResult<List<Event>> result = new QueryResult<>(success: true)
        List<Event> userFriendEvents
        List<Event> userFriendPublicEvents = new ArrayList<>()
        User user = token?.user
        int month = monthString.isInteger() ? monthString.toInteger() : -1
        Long userId
        if(userIdString == null) {
            userId = -2
        } else {
            userId = userIdString.isLong() ? userIdString.toLong() : -1
        }
        if(user != null) {
            if(userId == -2) {
                QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
            } else if(userId > 0) {
                User requestedUserFriend
                for(friend in user.following) {
                    if(friend.id == userId) {
                        requestedUserFriend = friend
                    }
                    if(requestedUserFriend != null && (month>0 && month<13)) {
                        userFriendEvents = Event.findAllById(requestedUserFriend.id)
                        for (event in userFriendEvents) {
                            if(event.isPublic == 0) {
                                userFriendPublicEvents.add(event)
                            }
                        }
                    }
                }
                result.data = userFriendPublicEvents
            } else {
                QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
            }
        } else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, result)
        }
        result
    }
 */
