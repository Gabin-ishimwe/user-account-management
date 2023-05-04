package com.app.dto;


import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthServiceKafkaProducer {
    private String firstName;
    private String lastName;
    private UUID userId;
}
