## LDAP Oauth sample

Sample project where user needs to login based on their ldap credentials.

Users:
- bob/password - customer
- ben/password - customer, admin

Endpoints:
- http://localhost:8080/
- http://localhost:8080/orders (checks for customer role)
- http://localhost:8080/admin (checks for admin role)

## Other

- Generate an RSA key
    ```
    openssl genrsa -out rsa.pem 2048
    ```
- Use `cat` to display it:
    ```
    cat rsa.pem
    
    -----BEGIN RSA PRIVATE KEY-----
    ...
    -----END RSA PRIVATE KEY-----
    ```
- display the public key:
    ```
    openssl rsa -in rsa.pem -pubout
    
    -----BEGIN PUBLIC KEY-----
    ...
    -----END PUBLIC KEY-----
    ```