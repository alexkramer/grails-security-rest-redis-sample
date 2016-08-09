import grails.security.rest.redis.CustomAccessTokenRenderer
import grails.security.rest.redis.FailedLoginListener
import grails.security.rest.redis.RestAuthenticationFailureHandler
import grails.security.rest.redis.SuccessfulLoginListener

beans = {

    /**
     * Spring Security Core/REST Bean Overrides
     */
    failedLoginListener(FailedLoginListener) {
        loginAttemptsService = ref('loginAttemptsService')
    }
    successfulLoginListener(SuccessfulLoginListener) {
        loginAttemptsService = ref('loginAttemptsService')
    }
    restAuthenticationFailureHandler(RestAuthenticationFailureHandler) {
        messageSource = ref('messageSource')
    }
    accessTokenJsonRenderer(CustomAccessTokenRenderer)
}
