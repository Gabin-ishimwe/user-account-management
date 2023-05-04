package com.app.kafka;

import com.app.dto.Status;
import com.app.dto.UpdateProfileEventDto;
import com.app.entities.Account;
import com.app.entities.AccountStatus;
import com.app.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountConsumerConfig {
    private final KafkaTemplate<String, UpdateProfileEventDto> kafkaTemplate;
    private final AccountRepository accountRepository;


    @KafkaListener(topics = "account-event")
    public void consumeProfileMessage(UpdateProfileEventDto updateProfileEventDto){
        log.info("Going to create profile " + updateProfileEventDto);
        try {
            Account account = Account.builder()
                    .userId(updateProfileEventDto.getUserId())
                    .status(AccountStatus.UNVERIFIED)
                    .build();
            accountRepository.save(account);
            log.info("account created-----");
        } catch (Exception e) {
            // TODO: failed to create account
            // TODO: send a compensation transaction to other service
            log.info("failed to create account ------------------");
            updateProfileEventDto.setStatus(Status.FAILURE);
            kafkaTemplate.send("failure-profile", updateProfileEventDto);
            log.info("compensation transaction sent");
        }
    }

}
