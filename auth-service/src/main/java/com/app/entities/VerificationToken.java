package com.app.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "verificationTokens")
@Data
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;

    @OneToOne(mappedBy = "verificationToken")
    private User user;

    public static final int EXPIRATION_TIME = 15;

    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public VerificationToken(String token) {
        this.token = token;
        this.expirationTime = this.getTokenExpirationTime();
    }

    private LocalDateTime getTokenExpirationTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.plusMinutes(EXPIRATION_TIME);
    }


}
