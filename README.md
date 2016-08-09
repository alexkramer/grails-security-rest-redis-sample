Grails Security Rest Redis Sample Application
=============================================
Grails 3 sample application which demonstrates some implementations of 
common Spring Security customizations. In particular this covers 
scenarios for securing a REST API using Redis for the token store.

Scenarios Covered
-----------------
1. Custom login response (default only provides the token object)
2. Locking a user after certain number of failed attempts
3. Returning a message when the login fails (default only provides the
HttpStatus code)