package com.fombico.sameauthandresourceserver;

import com.fombico.sameauthandresourceserver.authentication.LoginFilter;
import com.fombico.sameauthandresourceserver.authentication.OAuth2Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${signing-key}")
    String signingKey;

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // no csrf
                .csrf().disable()
                // replace login page with 401 status when authentication is required
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                // authenticate requests
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .addFilter(new LoginFilter(authenticationManager(), defaultTokenServices()))
                .addFilterAfter(new OAuth2Filter(defaultTokenServices()), LoginFilter.class)
                .formLogin().permitAll();
    }

    @Bean
    PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DefaultTokenServices defaultTokenServices() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(signingKey);
        jwtAccessTokenConverter.setVerifier(new MacSigner(signingKey));

        // configure access token here (refresh token, expiry, signing, etc)
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(new JwtTokenStore(jwtAccessTokenConverter));
        defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter);
        return defaultTokenServices;
    }
}
