package com.app.kafka;

import com.app.dto.AuthServiceKafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthEventConsumer {


    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "auth-group")
    public void consumeAuthEvent(AuthServiceKafkaProducer authServiceKafkaProducer) {
        /**
         * save user
         */
        log.info("Auth service event has been received and going to be processed");
    }
}
