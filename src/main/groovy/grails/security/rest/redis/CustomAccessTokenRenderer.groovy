package grails.security.rest.redis

import grails.converters.JSON
import grails.plugin.springsecurity.rest.token.AccessToken
import grails.plugin.springsecurity.rest.token.rendering.AccessTokenJsonRenderer
import grails.transaction.Transactional
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.util.Assert

/**
 * Created by ackramer19 on 8/7/16.
 */
class CustomAccessTokenRenderer implements AccessTokenJsonRenderer {

    @Override
    @Transactional
    String generateJson(AccessToken accessToken) {
        Assert.isInstanceOf(UserDetails, accessToken.principal, "A UserDetails implementation is required")
        UserDetails userDetails = accessToken.principal as UserDetails
        AppUser appUser = AppUser.findByUsername(userDetails.username)
        def result = [
                username    : userDetails.username,
                authorities : accessToken.authorities.collect { GrantedAuthority role -> role.authority },
                profile     : [
                        firstName: appUser.firstName,
                        lastName : appUser.lastName
                ],
                token_type  : 'Bearer',
                access_token: accessToken.accessToken
        ]

        if (accessToken.expiration) result.expires_in = accessToken.expiration

        if (accessToken.refreshToken) result.refresh_token = accessToken.refreshToken

        return (result as JSON).toString()
    }
}
