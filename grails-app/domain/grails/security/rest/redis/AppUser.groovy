package grails.security.rest.redis

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class AppUser implements Serializable {

    private static final long serialVersionUID = 1

    transient springSecurityService

    String username
    String password
    String firstName
    String lastName


    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    AppUser(String username, String password,String firstName, String lastName) {
        this()
        this.username = username
        this.password = password
        this.firstName = firstName
        this.lastName = lastName
    }

    Set<Role> getAuthorities() {
        AppUserRole.findAllByAppUser(this)*.role
    }

    def beforeInsert() {
        encodePassword()
    }

    def beforeUpdate() {
        if (isDirty('password')) {
            encodePassword()
        }
    }

    protected void encodePassword() {
        password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
    }

    static transients = ['springSecurityService']

    static constraints = {
        password blank: false, password: true
        username blank: false, unique: true
    }

    static mapping = {
        password column: '`password`'
    }
}

