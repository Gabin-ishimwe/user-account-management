package com.app;

import com.app.config.TwilioConfig;
import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@RequiredArgsConstructor
@EnableDiscoveryClient
public class AuthServiceApplication {

    private final TwilioConfig twilioConfig;

    @Value("${TWILIO_AUTH_TOKEN}")
    private final String twilioAuthToken;

    @PostConstruct
    public void initTwilio() {
        System.out.println("twilio auth 1" + twilioConfig.getAuthToken());
        System.out.println("twilio auth 2" + twilioAuthToken);
        Twilio.init(twilioConfig.getAccountSid(), !twilioConfig.getAuthToken().isEmpty() ? twilioConfig.getAuthToken() : twilioAuthToken);
    }
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class);
    }
}