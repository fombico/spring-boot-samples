# Spring-Boot PCF SSO Resource Server

A sample resource server, to be deployed onto PCF, that binds to the `Single Sign-on` Service in the PCF Marketplace.
A JWT is required to hit the endpoints.

## To Test
1. Create a Single Sign-On Service called `sso`
2. Push app.
3. Try to hit `{endpoint}/hello` and see a 401.
4. Get an access token:
    1. Run `cf env resource-server` and note the `p-identity`'s `auth_domain`, `client_id`, `client_secret`
    2. Run to get an access token:
    ```
    curl -k '{auth_domain}/oauth/token' -i -u '{clientId}:{clientSecret}' -X POST -H 'Accept: application/json' -d 'grant_type=client_credentials&response_type=token'
    ```
5. Use the access token in a request to `{endpoint}/hello` and see a 200. 