package grails.security.rest.redis

import groovy.json.StreamingJsonBuilder
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by akramer on 7/23/16.
 * The default implementation only returns HttpStatus codes.
 * This one will override the behavior to include a message in the json body.
 */
class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {

    def messageSource

    @Override
    void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {


        String msg
        if (exception instanceof AccountExpiredException) {
            msg = messageSource.getMessage('springSecurity.errors.login.expired', null, Locale.US)
        } else if (exception instanceof CredentialsExpiredException) {
            msg = messageSource.getMessage('springSecurity.errors.login.passwordExpired', null, Locale.US)
        } else if (exception instanceof DisabledException) {
            msg = messageSource.getMessage('springSecurity.errors.login.disabled', null, Locale.US)
        } else if (exception instanceof LockedException) {
            msg = messageSource.getMessage('springSecurity.errors.login.locked', null, Locale.US)
        } else {
            msg = messageSource.getMessage('springSecurity.errors.login.fail', null, Locale.US)
        }

        // build the response object
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = "application/json"
        def builder = new StreamingJsonBuilder(response.writer)
        builder.call {
            message msg
        }
    }
}
