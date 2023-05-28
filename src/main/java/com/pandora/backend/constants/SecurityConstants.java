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

    @Value("${spring.security.auth.whitelist}")
    private String[] whitelist;
}
