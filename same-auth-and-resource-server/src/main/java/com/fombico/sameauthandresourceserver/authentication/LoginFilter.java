package com.fombico.sameauthandresourceserver.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthorizationServerTokenServices tokenServices;

    public LoginFilter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices) {
        this.tokenServices = tokenServices;
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);

        Authentication authentication;
        if (username != null) {
            log.info("Login request as user '{}' via query params", username);
            authentication = super.attemptAuthentication(request, response);
        } else {
            try {
                LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
                username = loginRequest.username;
                log.info("Login request as user '{}' via request body", username);

                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, loginRequest.password);
                authentication = getAuthenticationManager().authenticate(authRequest);
            } catch (IOException e) {
                throw new BadCredentialsException(e.getMessage());
            }
        }

        log.info("User '{}' has authorities: {}", username, authentication.getAuthorities());

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        OAuth2AccessToken oAuth2AccessToken = getToken(authResult);
        writeTokenToResponse(response, oAuth2AccessToken);
    }

    private OAuth2AccessToken getToken(Authentication authentication) {
        Set<String> scopes = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        OAuth2Request oauth2Request = new OAuth2Request(null, authentication.getName(), authentication.getAuthorities(), true, scopes, null, null, null, null);

        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oauth2Request, authentication);
        OAuth2AccessToken oAuth2AccessToken = tokenServices.createAccessToken(oAuth2Authentication);

        log.info("token: {} ", oAuth2AccessToken.getValue());
        return oAuth2AccessToken;
    }

    private void writeTokenToResponse(HttpServletResponse response, OAuth2AccessToken oAuth2AccessToken) throws IOException {
        LoginResponse loginResponse = new LoginResponse(oAuth2AccessToken.getValue());
        String content = new ObjectMapper().writeValueAsString(loginResponse);
        response.getWriter().print(content);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Data
    @NoArgsConstructor
    static class LoginRequest {
        String username;
        String password;
    }

    @Data
    @AllArgsConstructor
    static class LoginResponse {
        String accessToken;
    }
}
