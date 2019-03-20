## Spring-Boot Resource Server that validates against PCF SSO

A sample resource server, to be deployed onto PCF, that validates Oauth JWTs against the `Single Sign-on` Service in the PCF Marketplace.
A JWT is required to hit the endpoints. Update the `application.yml` with your `key-set-uri`.

## To Test no-scopes
1. Create a Single Sign-On Service called `sso`
1. Register an app in the PCF `Single Sign-On` management page
    1. Application type is `Service-to-service`
    1. Authorization only has `uaa.resource`
    1. Once created, note the `App Id` and `App Secret`
1. Push app.
1. Try to hit `{endpoint}/hello` and see a 401.
1. Get an access token using `App Id` and `App Secret`. 
   You might need to bind to the sso server and check the environment to find `{auth_domain}`
    ```
    curl -k '{auth_domain}/oauth/token' -i -u '{appId}:{appSecret}' -X POST -H 'Accept: application/json' -d 'grant_type=client_credentials&response_type=token'
    ```
1. Use the access token in a request to `{endpoint}/hello` and see a 200. 


## To Test scopes
1. Try to hit `GET {endpoint}/read` or `POST {endpoint}/write` and see a 403
1. Create a `todo` Resource in the PCF `Single Sign-On` management page
    1. Add permissions for `read` and `write`
1. Register an app in the PCF `Single Sign-On` management page
    1. Application type is `Service-to-service`
    1. Authorization includes `uaa.resource`, `todo.read`, `todo.write`
    1. Once created, note the `App Id` and `App Secret`
1. Use the new `App Id` and `App Secret` to get a bearer token
1. Hit `GET {endpoint}/read` or `POST {endpoint}/write` and see a 200.

Hit `{endpoint}/info` and compare the `Granted Authorities` when using the two different access tokens
