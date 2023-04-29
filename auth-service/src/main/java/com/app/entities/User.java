package com.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(
            name = "email"
    )
    private String email;

    @Column(
            name = "password"
    )
    private String password;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "mfa_enabled")
    private boolean mfaEnabled;

    @ManyToMany(
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "user_role_mapping",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id"
            )
    )
    private List<Role> roles;

    @OneToOne(mappedBy = "user")
    private VerificationToken verificationToken;

    @OneToOne(mappedBy = "user")
    private PasswordResetToken passwordResetToken;

    @OneToOne(mappedBy = "user")
    private Otp otp;
}
