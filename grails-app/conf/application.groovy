// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'grails.security.rest.redis.AppUser'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'grails.security.rest.redis.AppUserRole'
grails.plugin.springsecurity.authority.className = 'grails.security.rest.redis.Role'

grails.plugin.springsecurity.controllerAnnotations.staticRules = [
        [pattern: '/',               access: ['permitAll']],
        [pattern: '/error',          access: ['permitAll']],
        [pattern: '/index',          access: ['permitAll']],
        [pattern: '/index.gsp',      access: ['permitAll']],
        [pattern: '/assets/**',      access: ['permitAll']],
        [pattern: '/**/js/**',       access: ['permitAll']],
        [pattern: '/**/css/**',      access: ['permitAll']],
        [pattern: '/**/images/**',   access: ['permitAll']],
        [pattern: '/**/favicon.ico', access: ['permitAll']],
        //spring rest security api end-point
        [pattern: '/api/logout',     access: ['isAuthenticated()']],
]

//Spring Security REST API grails plugin
grails.plugin.springsecurity.filterChain.chainMap = [
        //Stateless chain
        [ pattern: '/api/**', filters: 'JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-rememberMeAuthenticationFilter'],
]

grails.plugin.springsecurity.useSecurityEventListener = true
grails.plugin.springsecurity.rest.token.storage.redis.expiration = 3600
grails.plugin.springsecurity.rest.logout.endpointUrl = '/api/logout' //though default, without this the logout doesn't work for securityConfigType="Annotations"