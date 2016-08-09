package grails.security.rest.redis

import grails.core.GrailsApplication
import grails.transaction.Transactional
import redis.clients.jedis.Jedis

class LoginAttemptsService {

    GrailsApplication grailsApplication
    def redisService

    void logAttempt(AppUser user) {
        int maxLoginAttempts = grailsApplication.config.getProperty('maxLoginAttempts', Integer, 3)
        Long currentAttemptCount
        redisService.withRedis { Jedis redis ->
            currentAttemptCount = redis.incr("LoginAttempts-${user.id}")
        }
        log.info("Current attempt count for ${user.username} ${currentAttemptCount}")
        if (currentAttemptCount >= maxLoginAttempts) {
            lockAccount(user)
            clearAttempts(user)
        }
    }

    @Transactional
    void lockAccount(AppUser user) {
        log.info("Locking account for user ${user.username}")
        user.accountLocked = true
        user.save()
    }

    void clearAttempts(AppUser user) {
        redisService.withRedis { Jedis redis ->
            redis.del("LoginAttempts-${user.id}")
        }
    }
}
