package com.fombico.resourceserver

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer

@Configuration
@EnableWebSecurity
@EnableResourceServer
class OAuthSecurityConfig : WebSecurityConfigurerAdapter() {

    @Bean
    fun resourceServerConfigurerAdapter(): ResourceServerConfigurer {
        return object : ResourceServerConfigurerAdapter() {
            override fun configure(resources: ResourceServerSecurityConfigurer) {
                resources.resourceId("uaa")
            }
        }
    }
}