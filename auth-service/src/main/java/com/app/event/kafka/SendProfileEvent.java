package com.app.event.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SendProfileEvent {
    @Autowired
    private final Sinks.Many<SendProfileEventDto> profileEvent;

    public void publishProfileEvent(String firstName, String lastName, UUID userId) {
        SendProfileEventDto profileEventDto = SendProfileEventDto
                .builder()
                .firstName(firstName)
                .lastName(lastName)
                .userId(userId)
                .build();

        // emit event
        profileEvent.tryEmitNext(profileEventDto);
    }
}
