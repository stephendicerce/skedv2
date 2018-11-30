package organizer

import org.springframework.http.HttpStatus
import util.QueryResult

class UserController {

    static responseFormats = ['json', 'xml']

    PreconditionService preconditionService
    UserService userService

    def getUser(String accessToken, String first_name, String last_name, String email) {
        QueryResult<AuthToken> checks = new QueryResult<>()
        preconditionService.notNull(params, ['accessToken'], checks)
        preconditionService.accessToken(accessToken, checks)

        if(checks.success) {
            if(first_name || last_name || email) {
                QueryResult<List<User>> queryResult = userService.findUsersBy(first_name, last_name, email)
                if(queryResult.success) {
                    render(view: 'users', model: [token: checks.data, users: queryResult.data])
                } else {
                    render(view:'../failure', model: [errorCode: queryResult.errorCode, message: queryResult.message])
                }
            } else {
                render(view:'../failure', model:[errorCode: HttpStatus.BAD_REQUEST.value(), message: 'Must Specify at least one search parameter'])
            }
        } else {
            render(view: '../failure', model: [errorCode: checks.errorCode, message:  checks.message])
        }
    }

    def postUser(String accessToken, String email) {
        QueryResult<AuthToken> checks = new QueryResult<>()
        preconditionService.notNull(params, ['accessToken', 'email'], checks)
        preconditionService.accessToken(accessToken, checks)

        if(checks.success) {
            QueryResult<User> result = userService.createUser(email)
            if(result.success) {
                render(view: 'users', model: [token: checks.data, users: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view:  '../failure', model: [errorCode:  checks.errorCode, message: checks.message])
        }
    }

    def followUser(String accessToken, String userId) {
        def require = preconditionService.notNull(params, ['accessToken', 'userId'])
        def token = preconditionService.accessToken(accessToken, require).data
        if(require.success) {
            QueryResult<List<User>> result = userService.followUser(token, userId)
            if(result.success) {
                render(view: 'users', model: [token: token, users: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    def removeUserFromFollowing(String accessToken, String followerId) {
        def require = preconditionService.notNull(params, ['accessToken', 'followerId'])
        def token = preconditionService.accessToken(accessToken, require).data
        if(require.success) {
            QueryResult<User> result = userService.unfollowUser(token, followerId)
            if(result.success) {
                render(view: 'users', model: [token: token, users: [result.data]])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    def getFollowing(String accessToken) {
        def require = preconditionService.notNull(params, ['accessToken'])
        def token = preconditionService.accessToken(accessToken, require).data

        if(require.success) {
            def result = userService.getUsersThatYouAreFollowing(token)

            if(result.success) {
                render(view: 'users', model: [token: token, users: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }
}
