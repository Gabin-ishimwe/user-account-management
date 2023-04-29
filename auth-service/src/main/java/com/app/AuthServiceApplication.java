package com.app;

import com.app.config.TwilioConfig;
import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class AuthServiceApplication {

    private final TwilioConfig twilioConfig;

    @PostConstruct
    public void initTwilio() {
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
    }
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class);
    }
}