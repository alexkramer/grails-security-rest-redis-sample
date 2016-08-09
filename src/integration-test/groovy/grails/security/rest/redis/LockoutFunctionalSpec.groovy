package grails.security.rest.redis

import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.springframework.http.HttpStatus

@Integration
@Rollback
class LockoutFunctionalSpec extends GebSpec implements GebBootstrapTrait {

    def grailsApplication
    def messageSource
    static String lockoutUsername

    static Closure getSetupDataClosure() {
        return {
            AppUser.withTransaction {
                lockoutUsername = "LFS" + Long.toString(new Date().time, 36) + "@alexckramer.com"
                Role userRole = Role.findByAuthority('ROLE_USER') ?: new Role('ROLE_USER').save()

                // Create a test user
                AppUser testUser = new AppUser(lockoutUsername, 'IWillNeverRememberThis', 'Forgetfull', 'Snow').save()
                AppUserRole.create(testUser, userRole)
            }
        }
    }

    void "test logging in many times until lockout is reached"() {

        given: "the max number of tries to login"
        int maxAttempts = grailsApplication.config.getProperty('maxLoginAttempts', Integer, 3)

        when: "the user tries the password that many times"
        def rest = new HTTPBuilder()
        def responseJson
        (maxAttempts+1).times {
            rest.request("$baseUrl", Method.POST, ContentType.JSON) {
                uri.path = '/api/login'
                headers.Accept = "application/json"
                body = [
                        username: lockoutUsername,
                        password: 'NotTheRealPassword'
                ]

                response."${HttpStatus.UNAUTHORIZED.value()}" = { resp, json ->
                    responseJson = json
                }
            }

        }

        then: "the user should be locked out"
        responseJson.message == messageSource.getMessage('springSecurity.errors.login.locked', null, Locale.US)
    }
}
