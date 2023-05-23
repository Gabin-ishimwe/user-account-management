package com.app.event.registration;

import com.app.entities.User;
import com.app.services.UserService;
import com.app.services.VerificationTokenService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationEventListener implements ApplicationListener<RegistrationEvent> {

    private final UserService userService;
    private final JavaMailSender javaMailSender;

    private final VerificationTokenService verificationTokenService;
    @Override
    public void onApplicationEvent(RegistrationEvent event) {
        // get user
        User user = event.getResponseDto().getUser();
        // create token
        String verificationToken = UUID.randomUUID().toString();
        verificationTokenService.saveUserVerificationToken(user, verificationToken);
        // send email
        String url = "http://161.35.27.255" + "/api/v1/auth/verify-email?token=" + verificationToken;
        // send email
        try {
            sendVerificationEmail(url, user);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("Click the link to verify your email: {}", url);

    }
    public void sendVerificationEmail(String url, User user) throws MessagingException, UnsupportedEncodingException, MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "User Registration Service";
        String mailContent = "<p> Hello there </p>"+
                "<p>Thank you for registering with us,"+"" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
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
