package com.app.services;

import com.app.dto.AuthResponseDto;
import com.app.dto.SignInDto;
import com.app.dto.SignupDto;
import com.app.entities.User;
import com.app.entities.VerificationToken;
import com.app.exceptions.UserExistsException;
import com.app.repositories.UserRepository;
import com.app.repositories.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    @Transactional
    public AuthResponseDto userSignUp(SignupDto signupDto) throws UserExistsException {
        Optional<User> findUser = userRepository.findByEmail(signupDto.getEmail());
        if(findUser.isPresent()) throw new UserExistsException("User already exists");
        // create user in database
        User user = User.builder()
                .email(signupDto.getEmail())
                .password(signupDto.getPassword())
                .build();
        User createdUser = userRepository.save(user);

        AuthResponseDto responseDto = AuthResponseDto.builder()
                .message("User registered successful")
                .user(createdUser)
                .build();
        // send kafka stream to other service

        return responseDto;
    }

    public AuthResponseDto userSignIn(SignInDto signInDto){
        return null;
    }

    public void saveUserVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);
    }

    public String verifyUser(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken.getUser().isEnabled()) return "User has already been verified, please login";

        return validateToken(verificationToken);
    }

    public String validateToken(VerificationToken token) {
        if(token == null) {
            return "Invalid verification token";
        }
        if(token.getExpirationTime().isAfter(LocalDateTime.now())) {
            verificationTokenRepository.delete(token);
            return "Token already expired";
        }
        User user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        return "Email verified successful. Now you can login";
    }
}
