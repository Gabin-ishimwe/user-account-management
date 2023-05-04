package com.app.kafka;

import com.app.dto.AuthServiceKafkaProducer;
import com.app.dto.Status;
import com.app.dto.UpdateProfileEventDto;
import com.app.entities.Profile;
import com.app.repositories.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthEventConsumer {

    private final ProfileRepository profileRepository;

    private final KafkaTemplate<String, UpdateProfileEventDto> kafkaTemplate;
    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "auth-group")
    public void consumeAuthEvent(AuthServiceKafkaProducer authServiceKafkaProducer) {
        /**
         * create user profile
         */

        try {
            Profile createdProfile = profileRepository.save(Profile.builder()
                    .firstName(authServiceKafkaProducer.getFirstName())
                    .lastName(authServiceKafkaProducer.getLastName())
                    .userId(authServiceKafkaProducer.getUserId())
                    .profilePhoto("https://res.cloudinary.com/dmepvxtwv/image/upload/v1682596041/avatar_image_iyomy2.png")
                    .build());

            log.info("User profile created : " + createdProfile);
            kafkaTemplate.send("account-event", UpdateProfileEventDto.builder()
                            .userId(authServiceKafkaProducer.getUserId())
                            .profileId(createdProfile.getId())
                            .status(Status.SUCCESS)
                    .build());
            log.info("Event sent to account service --------------------- ");
        } catch (Exception e) {
            /**
             * send a compensation transaction to auth service
             */
            log.info("user profile failed----------------------------");
            kafkaTemplate.send("failure-profile", UpdateProfileEventDto.builder()
                            .status(Status.FAILURE)
                            .userId(authServiceKafkaProducer.getUserId())
                    .build());

        }

    }

    @KafkaListener(topics = "failure-profile")
    public void compensationTransaction(UpdateProfileEventDto updateProfileEventDto) {
        log.info("going to delete profile----------------");
        profileRepository.deleteById(updateProfileEventDto.getProfileId());
        log.info("profile deleted ---------------------");
    }
}
