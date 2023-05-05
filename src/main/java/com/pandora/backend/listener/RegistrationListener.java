package com.pandora.backend.listener;

import com.pandora.backend.constants.MailConstants;
import com.pandora.backend.model.event.OnRegistrationCompleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConstants mailConstants;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        String recipientAddress = event.getUser().getEmail();
        String subject = "\uD83D\uDD11 Pandora Confirmação de Registro";
        String confirmationUrl = "http://localhost:4200/registrationConfirm?token=" + event.getToken();
        String message = "Olá,\n\nPor favor, confirme seu e-mail com com o link abaixo. ";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(mailConstants.getMail_username());
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\n\n" + confirmationUrl + "\n\n\n Equipe Pandora");
        mailSender.send(email);
    }
}
