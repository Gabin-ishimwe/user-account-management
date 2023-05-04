package com.app.kafka;

import com.app.dto.AccountVerification;
import com.app.dto.UpdateProfileEventDto;
import com.app.entities.User;
import com.app.exceptions.NotFoundException;
import com.app.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final UserRepository userRepository;

    private final JavaMailSender javaMailSender;

    @KafkaListener(topics = "failure-profile")
    public void consumeMessage(UpdateProfileEventDto updateProfileEventDto) {
        log.info("Deleting created user ----------");
        userRepository.deleteById(updateProfileEventDto.getUserId());
        log.info("user has been deleted-----------");

    }

    @KafkaListener(topics = "account-verified")
    public void accountVerification(AccountVerification accountVerification) throws NotFoundException, MessagingException, UnsupportedEncodingException {
        log.info("Sending verification email ----------");
        User findUser = userRepository.findById(accountVerification.getUserId()).orElseThrow(() -> new NotFoundException("User not found"));
        String subject = "Account Verification";
        String senderName = "Account Service";
        String mailContent = "<p> Hello there </p>"+
                "<p>You account has been "+ accountVerification.getStatus() + "</p>" +
                "<p> Thank you";
        MimeMessage message = javaMailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("s.ishimwegabin@gmail.com", senderName);
        messageHelper.setTo(findUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        javaMailSender.send(message);

        log.info("Verification email sent --------------");
    }
}
