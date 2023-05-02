package com.app.kafka;

import com.app.dto.UpdateProfileEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountConsumerConfig {
    @KafkaListener(topics = "account-event")
    public void consumeProfileMessage(UpdateProfileEventDto updateProfileEventDto){
        log.info("Going to create profile " + updateProfileEventDto);
    }

}
