package com.app.event.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SendAccountEvent {

    private final Sinks.Many<SendAccountEventDto> accountEvent;

    public void publishAccountEvent(UUID profileId) {

    }
}
