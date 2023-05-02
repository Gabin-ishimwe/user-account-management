package com.app.kafka;

import com.app.dto.UpdateProfileEventDto;
import com.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final UserRepository userRepository;

    @KafkaListener(topics = "failure-profile")
    public void consumeMessage(UpdateProfileEventDto updateProfileEventDto) {
        log.info("Deleting created user ----------");
        userRepository.deleteById(updateProfileEventDto.getUserId());
        log.info("user has been deleted-----------");

    }
}
