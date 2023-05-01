package com.app.event.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendProfileEventDto {

    private String firstName;
    private String lastName;
    private UUID userId;
}
