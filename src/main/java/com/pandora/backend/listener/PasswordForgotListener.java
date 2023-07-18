package com.pandora.backend.listener;

import com.pandora.backend.constants.MailConstants;
import com.pandora.backend.model.event.OnPasswordForgotRequestEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class PasswordForgotListener implements ApplicationListener<OnPasswordForgotRequestEvent> {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConstants mailConstants;

    @Override
    public void onApplicationEvent(OnPasswordForgotRequestEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnPasswordForgotRequestEvent event) {
        String recipientAddress = event.getUser().getEmail();
        String subject = "\uD83D\uDD11 Pandora Redefinição de Senha";
        String confirmationUrl = "https://pandoralivros.dev.br/passwordResetConfirm?token=" + event.getToken();
        String message = "Olá,\n\nPor favor redefina sua senha com o link abaixo.";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(mailConstants.getMail_username());
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\n\n" + confirmationUrl + "\n\n\nPandora Team");
        mailSender.send(email);
    }
}
