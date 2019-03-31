package com.fombico.sameauthandresourceserver.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class OAuth2Filter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer";
    private final ResourceServerTokenServices tokenServices;

    public OAuth2Filter(ResourceServerTokenServices tokenServices) {
        this.tokenServices = tokenServices;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("Checking authorization");
        try {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            String token = header.substring(BEARER.length()).trim();

            OAuth2Authentication oAuth2Authentication = tokenServices.loadAuthentication(token);
            log.info("User '{}' has authorities: {}", oAuth2Authentication.getName(), oAuth2Authentication.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(oAuth2Authentication);
        } catch (Exception e) {
            log.warn(e.getMessage());
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(request, response);
    }

}
