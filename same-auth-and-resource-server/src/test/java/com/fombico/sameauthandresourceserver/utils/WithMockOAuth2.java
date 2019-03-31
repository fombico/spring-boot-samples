package com.fombico.sameauthandresourceserver.utils;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockOAuth2.WithMockOAuth2SecurityContextFactory.class)
public @interface WithMockOAuth2 {

    String[] scopes() default {};

    class WithMockOAuth2SecurityContextFactory implements WithSecurityContextFactory<WithMockOAuth2> {
        @Override
        public SecurityContext createSecurityContext(WithMockOAuth2 annotation) {
            Set<String> scopes = Stream.of(annotation.scopes()).collect(Collectors.toSet());

            OAuth2Request clientAuthentication = new OAuth2Request(Collections.emptyMap(), null, null, true, scopes, null, null, null, null);
            OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(clientAuthentication, null);
            oAuth2Authentication.setAuthenticated(true);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(oAuth2Authentication);
            return context;
        }
    }
}
