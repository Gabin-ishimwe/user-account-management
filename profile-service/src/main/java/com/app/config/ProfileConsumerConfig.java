package com.app.config;

import com.app.dto.ReceiveProfileEventDto;
import com.app.dto.Status;
import com.app.dto.UpdateProfileEventDto;
import com.app.entities.Profile;
import com.app.repositories.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class ProfileConsumerConfig {

    private final ProfileRepository profileRepository;

    public Function<Flux<ReceiveProfileEventDto>, Flux<UpdateProfileEventDto>> profileConsumerUpdate() {
        return profileUpdateEvent -> profileUpdateEvent.flatMap(this::processProfile);
    }

    private Mono<UpdateProfileEventDto> processProfile(ReceiveProfileEventDto receiveProfileEventDto) {
        // get user's information
        // create profile with initial information
        // if profile saved well --> emit event status == SUCCESS
        // else emit event status == FAILURE
        // send event to the account service

        Profile userProfile = Profile.builder()
                .firstName(receiveProfileEventDto.getFirstName())
                .lastName(receiveProfileEventDto.getLastName())
                .build();
        Profile savedProfile = profileRepository.save(userProfile);

        // emit another event to the account service
        UpdateProfileEventDto updateProfile = UpdateProfileEventDto.builder()
                .profileId(savedProfile.getId())
                .userId(savedProfile.getUserId())
                .status(Status.SUCCESS)
                .build();
        return Mono.fromSupplier(() -> result(receiveProfileEventDto));
    }

    private UpdateProfileEventDto result(ReceiveProfileEventDto receiveProfileEventDto) {
        System.out.println("Profile service sent event ------ " + receiveProfileEventDto);
        return null;
    }
}
