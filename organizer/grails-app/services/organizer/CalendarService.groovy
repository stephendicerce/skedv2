package organizer

import grails.gorm.transactions.Transactional
import org.springframework.http.HttpStatus
import util.QueryResult

@Transactional
class CalendarService {

    QueryResult<Calendar> createCalendar(AuthToken token, QueryResult<Calendar> result = new QueryResult<>(success: true)) {
        User calendarOwner = token?.user
        Calendar calendar = new Calendar()
        calendar.user = calendarOwner
        calendar.save(flush: true, failOnError: true)
        result.data = calendar

        result
    }

    QueryResult<Calendar> deleteCalendar(AuthToken token, String calendarId) {
        QueryResult<Calendar> res = new QueryResult<>()
        User requestingUser = token?.user
        long cid = calendarId.isLong() ? calendarId.toLong() : -1

        if(requestingUser != null && cid != -1) {
            Calendar calendar = Calendar.findById(cid)
            if(calendar != null) {
                if(isCalendarOwner(requestingUser, calendar)) {
                    doDelete(calendar, res)
                } else {
                    QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, res)
                }
            } else {
                QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, res)
            }
        } else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, res)
        }
    }

    private QueryResult<Calendar> doDelete(Calendar calendar, QueryResult<Calendar> result = new QueryResult<>(success: true)) {

    }

    private boolean isCalendarOwner(User user, Calendar calendar) {
        long userId = user.id
        long calendarOwnerId = calendar.user.id

        if(userId == calendarOwnerId)
            true
        else
            false
    }
}
