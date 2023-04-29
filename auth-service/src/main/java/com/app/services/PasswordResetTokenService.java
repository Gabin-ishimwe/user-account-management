package com.app.services;

import com.app.entities.PasswordResetToken;
import com.app.entities.User;
import com.app.exceptions.NotFoundException;
import com.app.repositories.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public void createUserPasswordResetToken(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(passwordResetToken);
    }

     public String validatePasswordResetToken(String token) throws NotFoundException {
        PasswordResetToken findPasswordResetToken = passwordResetTokenRepository.findByToken(token);
        if(findPasswordResetToken == null) throw new NotFoundException("Invalid reset token");
         if(findPasswordResetToken.getExpirationTime().isBefore(LocalDateTime.now())) {
             return "Token already expired";
         }
         return "Valid";
     }

     public Optional<User> findUserByToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
     }
}
