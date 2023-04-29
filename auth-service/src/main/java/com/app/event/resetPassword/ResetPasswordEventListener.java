package com.app.event.resetPassword;

import com.app.entities.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResetPasswordEventListener implements ApplicationListener<ResetPasswordEvent> {
    private final JavaMailSender javaMailSender;
    @Override
    public void onApplicationEvent(ResetPasswordEvent event) {
        try {
            sendPasswordResetVerificationEmail(event.getUrl(), event.getUser());
            log.info("Click here to reset your password " + event.getUrl());
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPasswordResetVerificationEmail(String url, User user) throws MessagingException, UnsupportedEncodingException, MessagingException, UnsupportedEncodingException {
        String subject = "Password Reset Verification";
        String senderName = "User Registration Service";
        String mailContent = "<p> Hello there </p>"+
                "<p><b>You recently requested to reset your password,</b>"+"" +
                "Please, follow the link below to complete the action.</p>"+
                "<a href=\"" +url+ "\">Reset password</a>"+
                "<p> Thank you <br> Users Registration Service";

        MimeMessage message = javaMailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("s.ishimwegabin@gmail.com", senderName);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        javaMailSender.send(message);
    }
}
