package organizer

import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap
import org.springframework.http.HttpStatus
import util.QueryResult

@Transactional
class PreconditionService {

    QueryResult notNull(GrailsParameterMap paramsMap, List<String> parameters, QueryResult results = new QueryResult(success: true)) {

        if(!results || !paramsMap || !parameters) {
            results = new QueryResult(success: false)
            results.message = "Null precondition parameters"
        }
        if(!results.success) {
            return results
        }

        if(parameters.size() > 0) {

            for(String param in parameters) {
                if(!paramsMap.containsKey(param)) {
                    results.with {
                        success = false
                        errorCode = HttpStatus.BAD_REQUEST.value()
                        message = "Required parameter $param is missing"
                    }
                    break
                }
            }
        }
        results
    }

    QueryResult<AuthToken> accessToken(String accessTokenString, QueryResult<AuthToken> results = new QueryResult<>(success: true)) {

        if(!results || !accessTokenString) {
            results = new QueryResult(success: false)
            results.message = "Null precondition parameters"
        }

        if(!results.success) {
            return results
        }

        if(accessTokenString == null)
            throw new IllegalArgumentException()
        AuthToken token = AuthToken.findByAccessToken(accessTokenString)

        if(token != null) {
            results.data = token
        } else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, results)
        }

        results
    }

    QueryResult<Long> convertToLong(String rawValue, String paramName, QueryResult<Long> results = new QueryResult<>(success: true)) {
        if(!results || !rawValue || !paramName) {
            results = new QueryResult(success: false)
            results.message = "Null precondition parmeters"
        }

        if(!results.success) {
            return results
        }

        if(rawValue != null && rawValue.isLong()) {
            results.data = rawValue.toLong()
        } else {
            results.success = false
            results.message = "Param $paramName should be a number. Value recieved: $rawValue"
        }
        results
    }
}