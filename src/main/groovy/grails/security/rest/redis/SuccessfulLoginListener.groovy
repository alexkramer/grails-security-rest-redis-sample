package grails.security.rest.redis

import grails.transaction.Transactional
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationSuccessEvent

class SuccessfulLoginListener implements ApplicationListener<AuthenticationSuccessEvent> {

    def loginAttemptsService

    @Transactional
    void onApplicationEvent(AuthenticationSuccessEvent event) {
        String username = event.authentication.name
        loginAttemptsService.clearAttempts(AppUser.findByUsername(username))
    }
}
