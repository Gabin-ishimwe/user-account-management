package com.app.event;

import com.app.entities.User;
import com.app.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationEventListener implements ApplicationListener<RegistrationEvent> {

    private final UserService userService;
    @Override
    public void onApplicationEvent(RegistrationEvent event) {
        // get user
        User user = event.getResponseDto().getUser();
        // create token
        String verificationToken = UUID.randomUUID().toString();
        userService.saveUserVerificationToken(user, verificationToken);
        // send email
        String url = event.getApplicationUrl() + "/api/v1/auth/verify-email?token=" + verificationToken;
        log.info("Click the link to verify your email: {}", url);

    }
}
