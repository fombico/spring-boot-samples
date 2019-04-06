package com.fombico.ftpsample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;

@Configuration
public class FtpConfig {

    @Bean
    DefaultFtpSessionFactory defaultFtpSessionFactory(
            @Value("${ftp.host}") String host,
            @Value("${ftp.port}") int port,
            @Value("${ftp.username}") String username,
            @Value("${ftp.password}") String password) {
        DefaultFtpSessionFactory defaultFtpSessionFactory = new DefaultFtpSessionFactory();
        defaultFtpSessionFactory.setHost(host);
        defaultFtpSessionFactory.setPort(port);
        defaultFtpSessionFactory.setUsername(username);
        defaultFtpSessionFactory.setPassword(password);
        return defaultFtpSessionFactory;
    }

}
