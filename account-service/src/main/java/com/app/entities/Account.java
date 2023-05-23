package com.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "passport_number")
    private String passportNumber;

    @Column(name = "national_id")
    private String nationalId;

    private String document;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "is_verified")
    private boolean isVerified;

    private UUID userId;
}
