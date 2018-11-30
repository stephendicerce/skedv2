package organizer

import grails.gorm.transactions.Transactional
import org.springframework.http.HttpStatus
import util.Pair
import util.QueryResult

@Transactional
class UserService {

    class UserErrors {
        final static String INVALID_ACCESS_TOKEN = "Access token invalid"
        final static String USER_NOT_FOUND = "User not found with given token"
    }

    /**
     *
     * @param token
     * @return
     */
    QueryResult<User> getUser(String token) {
        QueryResult queryResult = new QueryResult()
        queryResult.success = false
        AuthToken authToken = AuthToken.findByAccessToken(token)
        if(authToken == null) {
            queryResult.message = UserErrors.INVALID_ACCESS_TOKEN
            queryResult.errorCode = HttpStatus.BAD_REQUEST.value()
            return queryResult
        }

        User user = authToken.user

        if(user == null) {
            queryResult.message = UserErrors.USER_NOT_FOUND
            queryResult.errorCode = HttpStatus.INTERNAL_SERVER_ERROR.value()
            return queryResult
        }
        queryResult.data = user
        queryResult.success = true
        return queryResult
    }

    User getOrMakeByEmail(String email) {
        User user = User.findByEmail(email)

        if(user == null) {
            user = new User(email: email)
            user.save(flush: true, failOnError: true)
        }
        return user
    }

    /**
     *
     * @param subj
     * @param first
     * @param last
     * @param imageUrl
     * @param email
     * @return
     */
    Optional<Pair<User, AuthToken>> getMakeOrUpdate(String subj, String first, String last, String imageUrl, String email) {
        User user
        AuthToken token
        Calendar calendar

        token = AuthToken.findBySubject(subj)

        // we have found an auth object. Therefore we have this user and they've signed in before.
        if (token != null) {
            println "user located"
            user = token.user
            calendar = user.calendar
            if (user.email != email) {
                user.email = email
            }
            token.accessToken = UUID.randomUUID()
            token.save(flush: true, failOnError: true)
        } else {
            //println "new or pre-loaded user"
            // two possible situations here. This is a new user or it's a pre-loaded user
            //signing in for the first time.

            user = User.findByEmail(email)
            if (user == null) { //it's a new user
                println "new user"
                calendar = new Calendar()
                user = new User(firstName: first, lastName: last, imageUrl: imageUrl, email: email, calendar: calendar)
            } else {
                println "no user found."
            }
        }
        if(calendar != null) {
            user = user.save(flush: true, failOnError: true)
            calendar.save(flush: true, failOnError: true)
        }
        if (user.authToken == null) {
            user.setAuthToken(new AuthToken(subject: subj, accessToken: UUID.randomUUID()))
            user = user.save(flush: true, failOnError: true)
        }

        token = user.authToken

        user != null ? Optional.of(new Pair<User, AuthToken>(user, token))
                : Optional.empty()

    }

    /**
     *
     * @param first
     * @param last
     * @param email
     * @return
     */
    QueryResult<List<User>> findUsersBy(String first, String last, String email) {
        QueryResult<List<User>> result


        def users = User.createCriteria().list {
            if (first) {
                like('firstName', first.concat("%"))
            }

            if (last) {
                like('lastName', last.concat("%"))
            }

            if (email) {
                eq('email', email.concat("%"))
            }
        } as List<User>

        result = new QueryResult<>(success: true, data: users)

        result
    }

    /**
     *
     * @param email
     * @return
     */
    QueryResult<User> createUser(String email) {
        QueryResult<User> result
        if(User.findByEmail(email) == null) {

            User temp = new User(email: email)
            temp.save(flush: true)
            result = new QueryResult<>(success: true, data: temp)
        } else {
            result = new QueryResult<>(success: false, errorCode: HttpStatus.BAD_REQUEST.value(), message: "Email already exists")

        }
        result
    }

    /**
     *
     * @param token
     * @return
     */
    QueryResult<User> findUser(AuthToken token) {
        QueryResult<User> result = new QueryResult<>()
        User user = token?.user
        if(!user) {
            return QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
        }
        result.data = user
        result
    }

    /**
     *
     * @param token
     * @param userIdString
     * @return
     */
    QueryResult<List<User>> followUser(AuthToken token, String userIdString) {
        QueryResult<List<User>> result = new QueryResult<>(success: true)
        Long userId = userIdString.isLong() ? userIdString.toLong() : -1
        User requestingUser = token?.user

        if(requestingUser != null) {
            if (userId != requestingUser.id) {
                for (user in requestingUser.following) {
                    if (user.id == userId) {
                        result = new QueryResult<>(success: false, errorCode: HttpStatus.BAD_REQUEST.value(), message: "Already following this user")
                        result
                    }
                }
                User newUser = User.findById(userId)
                if(newUser != null) {
                    requestingUser.following.add(newUser)


                    requestingUser.save(flush:true, failOnError: true)


                    result.data = requestingUser.following
                } else {
                    result = new QueryResult<>(success: false, errorCode: HttpStatus.BAD_REQUEST.value(), message: "User not found")
                }
            } else {
                result = new QueryResult<>(success: false, errorCode: HttpStatus.BAD_REQUEST.value(), message: "You can not follow yourself.")
            }
        } else {
            result = new QueryResult<>(success: false, errorCode: HttpStatus.UNAUTHORIZED.value(), message: "Access Token does not match any known applicationUsers, check your log in status.")
        }
         return result
    }

    /**
     *
     * @param token
     * @return
     */
    QueryResult<List<User>> getUsersThatYouAreFollowing(AuthToken token) {
       QueryResult<List<User>> result = new QueryResult<>(success: true)
        User requestingUser = token?.user

        if(requestingUser != null) {
            //List<User> users = requestingUser.following
            for(user in requestingUser.following){
                println user.firstName
                println user.lastName
                println user.email
                println user.imageUrl
            }

            result.data = requestingUser.following
        } else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, result)
        }
         return result
    }

    QueryResult<User> unfollowUser(AuthToken token, String followerIdString) {
        Long followerId = followerIdString.isLong() ? followerIdString.toLong() : -1
        QueryResult<User> result = new QueryResult<>(success: true)
        User requestingUser = token?.user
        User unfollowedUser

        if(requestingUser != null) {
            for(user in requestingUser.following) {
                if(user.id == followerId) {
                    unfollowedUser = user
                    requestingUser.following.remove(user)

                }
            }
            if(unfollowedUser == null) {
                QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
            } else {
                result.data = unfollowedUser
            }
        } else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, result)
        }
        return result
    }

}