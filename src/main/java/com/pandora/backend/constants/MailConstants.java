package com.pandora.backend.constants;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class MailConstants {
    @Value("${spring.mail.host_address}")
    private String hostAddress;

    @Value("${spring.mail.username}")
    private String mail_username;
}
