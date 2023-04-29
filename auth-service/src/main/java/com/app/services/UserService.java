package com.app.services;

import com.app.dto.AuthResponseDto;
import com.app.dto.SignInDto;
import com.app.dto.SignupDto;
import com.app.entities.PasswordResetToken;
import com.app.entities.User;
import com.app.entities.VerificationToken;
import com.app.event.resetPassword.ResetPasswordEvent;
import com.app.exceptions.NotFoundException;
import com.app.exceptions.UserAuthException;
import com.app.exceptions.UserExistsException;
import com.app.repositories.RoleRepository;
import com.app.repositories.UserRepository;
import com.app.repositories.VerificationTokenRepository;
import com.app.utils.JwtUserDetailService;
import com.app.utils.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final JwtUtil jwtUtil;

    private final JwtUserDetailService jwtUserDetailService;

    private final AuthenticationManager authenticationManager;

    private final PasswordResetTokenService passwordResetTokenService;

    private final ApplicationEventPublisher applicationEventPublisher;


    @Transactional
    public AuthResponseDto userSignUp(SignupDto signupDto) throws UserExistsException {
        Optional<User> findUser = userRepository.findByEmail(signupDto.getEmail());
        if(findUser.isPresent()) throw new UserExistsException("User already exists");
        // create user in database
        User user = User.builder()
                .email(signupDto.getEmail())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .build();
        User createdUser = userRepository.save(user);

        // send kafka stream to other service

        return AuthResponseDto.builder()
                .message("Verify your email")
                .user(createdUser)
                .build();
    }

    public AuthResponseDto userSignIn(SignInDto signInDto) throws UserAuthException {
        String userEmail = signInDto.getEmail();
        String password = signInDto.getPassword();
        Optional<User> findUser = userRepository.findByEmail(userEmail);
        if(findUser.isEmpty()) throw new UserAuthException("Invalid Credential, Try again");

        if(!findUser.get().isEnabled()) throw new UserAuthException("Account isn't verified");

        boolean passwordVerification = passwordEncoder.matches(password, findUser.get().getPassword());
        if(!passwordVerification) throw new UserAuthException("Invalid Credential, Try again");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail, password));
        return createJwt("User Logged in Successfully", findUser.get());
    }

    public AuthResponseDto createJwt(String message, User user) {
        String userEmail = user.getEmail();
        UserDetails userDetails = jwtUserDetailService.loadUserByUsername(userEmail);
        String token = jwtUtil.generateToken(userDetails);
        return AuthResponseDto.builder()
                .message(message)
                .token(token)
                .build();

    }

//    public void saveUserVerificationToken(User user, String token) {
//        VerificationToken verificationToken = new VerificationToken(token, user);
//        verificationTokenRepository.save(verificationToken);
//    }
//
//    public String verifyUser(String token) throws Exception {
//        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
//        if(verificationToken == null) throw new NotFoundException("Verification token doesn't exist");
//        if(verificationToken.getUser().isEnabled()) return "User has already been verified, please login";
//
//        return validateToken(verificationToken);
//    }
//
//    public String validateToken(VerificationToken token) {
//        if(token.getExpirationTime().isBefore(LocalDateTime.now())) {
//            verificationTokenRepository.delete(token);
//            return "Token already expired";
//        }
//        User user = token.getUser();
//        user.setEnabled(true);
//        userRepository.save(user);
//        return "Email verified successful. Now you can login";
//    }


    public String forgotPasswordRequest(String email, String url) throws NotFoundException {
        System.out.println(email);
        Optional<User> findUser = userRepository.findByEmail(email);
        System.out.println(findUser.isPresent());
        if(findUser.isEmpty()) throw new NotFoundException("User doesn't exist");
        String passwordResetToken = UUID.randomUUID().toString();
        passwordResetTokenService.createUserPasswordResetToken(findUser.get(), passwordResetToken);
        String resetUrl = url + "/api/v1/auth/reset-password?token=" + passwordResetToken;
        applicationEventPublisher.publishEvent(new ResetPasswordEvent(findUser.get(), resetUrl));
        return "Reset password link sent on your email";
    }

    public String userResetPassword(String token, String newPassword) throws NotFoundException {
        String isTokenValid = passwordResetTokenService.validatePasswordResetToken(token);
        if(!Objects.equals(isTokenValid, "Valid")) return isTokenValid;
        Optional<User> user = passwordResetTokenService.findUserByToken(token);
        if(user.isEmpty()) return "Invalid token reference";

        user.get().setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user.get());

        return "Password has been reset successful";
    }
}
