package organizer

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import org.springframework.http.HttpStatus
import util.QueryResult

@Transactional
class VerifierService {

    class AuthErrors {
        final static String TOKEN_VERIFICATION = 'Token verification error'
        final static String TOKEN_INTEGRITY = 'Token integrity error'
        final static String UNVERIFIED_EMAIL = 'Unverified user email'
    }

    GrailsApplication grailsApplication

    /**
     * Method to get verification results for an id token string
     * @param idTokenString the string based id token to be verified
     * @return An object containing information about the verification of the requesting user
     */
    QueryResult<GoogleIdToken> getVerifiedResults(String idTokenString) {
        QueryResult<GoogleIdToken> data = new QueryResult<>()
        verifyIdToken(idTokenString, data)
        verifyIdTokenIntegrity(data)
        verifyEmail(data)
        data
    }

    /**
     * A method to verify the id token using google's own api
     * @param idTokenString a string representing the id token to check
     * @param data the object which the results will get sent to
     */
    private void verifyIdToken(String idTokenString, QueryResult<GoogleIdToken> data) {

        if(!data.success) {
            return
        }

        if(idTokenString == null) {
            data.success = false
            data.message = "Missing parameter id token"
            data.errorCode = HttpStatus.BAD_REQUEST.value()
            return
        }

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), new JacksonFactory()).setAudience([grailsApplication.config.getProperty("googleauth.clientId")]).build()

        GoogleIdToken temp

        try {
            temp = verifier.verify(idTokenString)
        } catch(Exception e) {
            temp = null
        }
        if(temp == null) {
            data.success = false
            data.message = AuthErrors.TOKEN_VERIFICATION
            data.errorCode = HttpStatus.BAD_REQUEST.value()
        } else {
            data.data = temp
        }

    }

    /**
     * A method to check the integrity of the token data by checking against the issuer and audience
     * @param data the object which the results will get sent to
     */
    private void verifyIdTokenIntegrity(QueryResult<GoogleIdToken> data) {

        if (!data.success || data.data == null) {
            return
        }

        GoogleIdToken token = data.data
        def passed = false

        if(token.verifyAudience([grailsApplication.config.getProperty("googleauth.clientId")])) {
            if(token.verifyIssuer(grailsApplication.config.getProperty("googleauth.issuer"))) {
                passed = true
            }
        }

        if(!passed) {
            data.success = false
            data.message = AuthErrors.TOKEN_INTEGRITY
            data.errorCode = HttpStatus.BAD_REQUEST.value()
        }
    }

    /**
     * verifies the requesting user's email
     * @param data the object which the results will get sent to
     */
    private void verifyEmail(QueryResult<GoogleIdToken> data) {
        if(!data.success || data.data == null) {
            return
        }
        GoogleIdToken.Payload payload = data.data.payload
        Boolean passed = false

        if(payload.getEmailVerified()) {
            passed = true
        } else {
            data.message = AuthErrors.UNVERIFIED_EMAIL
        }

        if(!passed) {
            data.success = passed
            data.errorCode = HttpStatus.BAD_REQUEST.value()
        }
    }
}