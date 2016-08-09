package grails.security.rest.redis

import grails.transaction.Transactional
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent

class FailedLoginListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    def loginAttemptsService

    /**
     * The transaction annotation is required to get access to GORM
     * @param event
     */
    @Transactional
    void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        AppUser user = AppUser.findByUsername(event.authentication.name)
        //The username not found also throws this error so it is possible that the AppUser does not exist
        if (user) loginAttemptsService.logAttempt(user)
    }
}
