package com.app.kafka;

import com.app.dto.AuthServiceKafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerConfig {

    @Value("${spring.kafka.topic.name}")
    private String topicName;
    private final KafkaTemplate<String, AuthServiceKafkaProducer> template;

    public void sendMessage(AuthServiceKafkaProducer producer) {
        Message<AuthServiceKafkaProducer> message = MessageBuilder
                .withPayload(producer)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .build();

        template.send(message);
    }
}
