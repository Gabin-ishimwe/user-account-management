package com.app.config;

import com.app.event.kafka.SendProfileEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

@Configuration
public class AuthPublisherConfig {

    @Bean
    public Sinks.Many<SendProfileEvent> sendEvent() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }

    @Bean
    public Supplier<Flux<SendProfileEvent>> sendProfileSupplier(Sinks.Many<SendProfileEvent> sinks) {
        return sinks::asFlux;
    }
}
