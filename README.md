# Sample JWT Server #


### Endpoints ###
* http://localhost:9093/attgroup/api/auth/authenticate - for authenticating users
* http://localhost:9093/attgroup/api/auth/register - to register new user

### How to run ###
~~~ bash
  java -jar app-0.0.1-SNAPSHOT.jar 
~~~

### JWT Expiration Configuration ###
~~~ yaml
app:
  jwt:
    jwtSecret: attgroup
    jwtExpiration: 10000 #in milliseconds
 ~~~