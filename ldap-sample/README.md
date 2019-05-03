# LDAP Sample

## Running the app
- There are two users that can log in
    - ben/password (manager, developer)
    - bob/password (developer)
- There are three endpoints
    - http://localhost:8080 - shows username, DN, authorities
    - http://localhost:8080/dev - only accessible with developer role
    - http://localhost:8080/manager - only accessible with manager role 

## Learnings
- used to `PasswordEncoderFactories` to create a `PasswordEncoder` that supports multiple password encodings
    - e.g. ben/password uses bcrypt while bob/password uses pbkdf2  
- ldap configuration in `WebSecurityConfig`
    - `userDnPatterns` - affects user search
    - `groupSearchBase` - helps determine the spring security role the user will have
- Uses [UnboundID LDAP SDK for Java](https://ldap.com/unboundid-ldap-sdk-for-java/) as an LDAP server
- Uses a sample .ldif to populate the LDAP server with users