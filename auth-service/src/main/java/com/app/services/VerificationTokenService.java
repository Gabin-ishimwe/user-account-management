package com.app.services;

import com.app.entities.User;
import com.app.entities.VerificationToken;
import com.app.exceptions.NotFoundException;
import com.app.repositories.UserRepository;
import com.app.repositories.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    public void saveUserVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);
    }

    public String verifyUser(String token) throws Exception {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken == null) throw new NotFoundException("Verification token doesn't exist");
        if(verificationToken.getUser().isEnabled()) return "User has already been verified, please login";

        return validateToken(verificationToken);
    }

    public String validateToken(VerificationToken token) {
        if(token.getExpirationTime().isBefore(LocalDateTime.now())) {
            verificationTokenRepository.delete(token);
            return "Token already expired";
        }
        User user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        return "Email verified successful. Now you can login";
    }
}
