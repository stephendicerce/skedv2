package organizer

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import util.Pair
import util.QueryResult


class AuthController {
    static responseFormats = ['json', 'xml']

    VerifierService verifierService
    UserService userService

    def auth(String idToken) {
        //println "auth"
        QueryResult<GoogleIdToken> data = verifierService.getVerifiedResults(idToken)
        if(data.success) {
            GoogleIdToken.Payload payload = data.data.payload
            String subj = payload.getSubject()
            String first = payload.get("given_name").toString()
            String last = payload.get("family_name").toString()
            String imageUrl = payload.get("picture").toString()
            String email = payload.getEmail()

            //println first
            //println last
            //println email

            Optional<Pair<User, AuthToken>> optionalInfo = userService.getMakeOrUpdate(subj, first, last, imageUrl, email)

            if(optionalInfo.isPresent()) {
                Pair<User, AuthToken> info = optionalInfo.get()
                session.setAttribute("access", info.value.accessToken)
                render(view: 'authUser', model: [token: info.value])
            } else {
                render(view: '../error')
            }
        } else {
            render(view: '../failure', model: [errorCode: data.errorCode, message: data.message])
        }
    }

    def current() {
        //println "current"
        String token = session.getAttribute("access").toString()
        if(token != null) {
            QueryResult<User> result = userService.getUser(token)
            if(result.success) {
                render(view: 'user', model: [user: result.data])
            } else {
                render(view: '/failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            logout()
        }
    }

    def logout() {
        //println "logout"
        String access = session.getAttribute("access")
        if(access != null) {
            session.removeAttribute("access")
        }
        session.invalidate()
        redirect(controller: 'application', action: 'landing')
    }
}
