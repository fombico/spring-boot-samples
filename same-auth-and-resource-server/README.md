## Same Auth and Resource Server

A sample project with a combined auth and resource server:
- As an auth server, it authenticates a username/password and issues an oauth2 token
- As a resource server, it also requires a valid oauth2 token to access the apis

### MethodSecurityConfig
- `@EnableGlobalMethodSecurity` - lets us use `@PreAuthorize` in our apis for checking authority/scope
- overriding `createExpressionHandler` - lets us use OAuth2 SpEL in tests/code

### ApiController
- authentication required for all endpoints
- `/api/info` - authentication only, not interested in authority/scope
- `/api/user` - requires 'user' authority
- `/api/admin` - requires 'admin' authority
- `/api/scope/user` - requires 'user' scope (oauth2)
- `/api/scope/admin` - requires 'admin' scope (oauth2)

### AccountUserService
- implements `UserDetailsService`
- inserts the user/password into an in-memory h2 db:
	- fred/fred (user authority)
	- admin/admin (admin authority)

### WebSecurityConfig
- configures `/h2-console` endpoint to ignore all auth
- removes basic-auth login page via `authenticationEntryPoint`
- adds the `LoginFilter` and `OAuth2Filter`
- configures bCrypt for passwords
- configures a `DefaultTokenServices` for issuing/verifying oauth2 tokens

### LoginFilter
- authenticates calls when POSTing to a `/login`
- can read username and password via query params or json body
- issues an oAuth2 token

### OAuth2Filter
- reads oauth2 token from the Authorization header and uses it to authenticate user


## Tests

### ApiControllerTest
- tests apis by checking authority and scope
- `@ComponentScan.Filter` is needed in `@WebMvcTest`, otherwise it won't see `MethodSecurityConfig` (which would result in  all `@PreAuthorize` usage would be ignored in the tests)
- uses `@WithMockOAuth2` instead of `@WithMockUser` for api tests that check scope (oauth2)

### WithMockOAuth2
- needed for testing `@PreAuthorize("#oauth2.hasScope('user')")` on apis

### NoWebSecurityConfig
- disables the `LoginFilter` and `OAuth2Filter`
- otherwise, it will apply those filters on the ApiControllerTest
- does not apply in the `WebSecurityConfigIntegrationTest`

### WebSecurityConfigIntegrationTest
- integration test (with mock `UserDetailsService`) that tests the login and oauth2 filters
- tests jwt tokens from login against apis
