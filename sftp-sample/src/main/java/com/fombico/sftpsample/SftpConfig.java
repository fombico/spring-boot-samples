package com.fombico.sftpsample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;

@Configuration
public class SftpConfig {
    @Bean
    DefaultSftpSessionFactory defaultSftpSessionFactory(
            @Value("${sftp.host}") String host,
            @Value("${sftp.port}") int port,
            @Value("${sftp.username}") String username,
            @Value("${sftp.password}") String password) {
        DefaultSftpSessionFactory defaultSftpSessionFactory = new DefaultSftpSessionFactory(false);
        defaultSftpSessionFactory.setHost(host);
        defaultSftpSessionFactory.setPort(port);
        defaultSftpSessionFactory.setUser(username);
        defaultSftpSessionFactory.setPassword(password);
        defaultSftpSessionFactory.setAllowUnknownKeys(true);
        return defaultSftpSessionFactory;
    }
}
