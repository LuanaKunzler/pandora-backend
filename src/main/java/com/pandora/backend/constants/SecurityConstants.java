package com.pandora.backend.constants;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("classpath:application.yml")
public class SecurityConstants {
    @Value("${spring.security.public-pattern}")
    private String publicPattern;

    @Value("${spring.security.auth.username}")
    private String authUsername;

    @Value("${spring.security.auth.url}")
    private String authUrl;

    @Value("${spring.security.auth.client_id}")
    private String clientId;

    @Value("${spring.security.auth.client_password}")
    private String clientPassword;

    @Value("${spring.security.auth.connection_timeout}")
    private String connectionTimeout;

    @Value("${spring.security.auth.read_timeout}")
    private String readTimeout;

    @Value("${spring.security.auth.whitelist}")
    private String[] whitelist;
}
