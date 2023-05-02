package com.app.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerConfig {

    @KafkaListener(topics = "auth-server", groupId = "")
    public void consumeMessage(String message) {

    }
}
