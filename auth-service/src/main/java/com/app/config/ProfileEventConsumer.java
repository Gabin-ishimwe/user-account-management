package com.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class ProfileEventConsumer {
    @Bean
    public Consumer<UpdateProfileEventDto> profileEventConsuming() {
        // testing logic
        // process the event from profile service
        // check status is SUCCEED => do an action
        // if FAILED => do another action
        return (updateProfileEventDto) -> handleConsumerResponse(event->{
            System.out.println("Here to debug and understand--------");
        });
    }

    public void handleConsumerResponse(Consumer<UpdateProfileEventDto> consumeProfileEvent) {
        consumeProfileEvent.andThen(this::handleConsumingResponse);
    }

    private void handleConsumingResponse(UpdateProfileEventDto updateProfileEventDto) {
        System.out.println("Event has arrived back in auth-service ----- " + updateProfileEventDto);
    }
}
