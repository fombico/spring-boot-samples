package com.fombico.sameauthandresourceserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Order(50)
@Configuration
@Profile("!integrationTest")
public class NoWebSecurityConfig extends WebSecurityConfigurerAdapter {

}