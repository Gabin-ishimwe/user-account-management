package com.app.services;

import com.app.dto.AuthResponseDto;
import com.app.dto.OtpRequestDto;
import com.app.dto.SignInDto;
import com.app.dto.SignupDto;
import com.app.entities.*;
import com.app.event.kafka.SendProfileEvent;
import com.app.event.resetPassword.ResetPasswordEvent;
import com.app.exceptions.NotFoundException;
import com.app.exceptions.UserAuthException;
import com.app.exceptions.UserExistsException;
import com.app.repositories.*;
import com.app.utils.JwtUserDetailService;
import com.app.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

    private final TwilioService twilioService;

    private final OtpRepository otpRepository;

    private final TokenRepository tokenRepository;

    private final SendProfileEvent profileEvent;


    @Transactional
    public AuthResponseDto userSignUp(SignupDto signupDto) throws UserExistsException {
        Optional<User> findUser = userRepository.findByEmail(signupDto.getEmail());
        if(findUser.isPresent()) throw new UserExistsException("User already exists");
        // create user in database
        User user = User.builder()
                .email(signupDto.getEmail())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .contactNumber(signupDto.getPhoneNumber())
                .build();
        User createdUser = userRepository.save(user);

        // send kafka stream to other service
        profileEvent.publishProfileEvent(
                signupDto.getFirstName(),
                signupDto.getLastName(),
                createdUser.getId()
        );

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

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail, password));
        if(!authenticate.isAuthenticated()) throw new UsernameNotFoundException("Bad Credentials, Try again");
        revokeUserTokens(findUser.get());
        return createJwt("User logged in successfully", findUser.get());
    }

    public AuthResponseDto createJwt(String message, User user) {
        String token = jwtUtil.generateToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
        Token saveToken = Token.builder()
                .user(user)
                .token(token)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();

        tokenRepository.save(saveToken);
        return AuthResponseDto.builder()
                .message(message)
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();

    }

    public String  forgotPasswordRequest(String email, String url) throws NotFoundException {
        System.out.println(email);
        Optional<User> findUser = userRepository.findByEmail(email);
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

    public String enableMfa(OtpRequestDto otpRequestDto) throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null && !authentication.isAuthenticated()) return "User not authenticated";

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Optional<User> findUser = userRepository.findByEmail(userDetails.getUsername());
        if(findUser.isEmpty()) throw new NotFoundException("User not found");
        findUser.get().setMfaEnabled(true);
        if(!otpRequestDto.getPhoneNumber().isEmpty()) findUser.get().setContactNumber(otpRequestDto.getPhoneNumber());
        userRepository.save(findUser.get());
        return "Multi factor authentication enabled";
    }

    public String userOtp(OtpRequestDto otpRequestDto) throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null && !authentication.isAuthenticated()) return "User not authenticated";

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Optional<User> findUser = userRepository.findByEmail(userDetails.getUsername());
        if(findUser.isEmpty()) throw new NotFoundException("User not found");
        return twilioService.sendOtpPasssword(otpRequestDto, findUser.get());
    }

    public AuthResponseDto userValidateOtp(String otp) throws NotFoundException, UserAuthException {
        Otp otpValidation = twilioService.validateOtp(otp);
        User userOtp = otpValidation.getUser();
        AuthResponseDto responseDto = createJwt("User logged in with OTP", userOtp);
        otpRepository.delete(otpValidation);
        return responseDto;
    }

    public void revokeUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllUserTokens(user.getId());
        if(validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public AuthResponseDto createRefreshToken(HttpServletRequest request, HttpServletResponse response) throws UserAuthException {
        String header = request.getHeader("Authorization");
        String refreshToken = null;
        String userEmail = null;
        // check if header has keyword 'bearer' in it
        if(header == null || !header.startsWith("Bearer ")) {
            throw new UserAuthException("User not authenticated");
        }
        refreshToken = header.split(" ")[1];
        // extracting payload from token
        userEmail = jwtUtil.getUserNameFromToken(refreshToken);
        if(userEmail != null) {
            User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserAuthException("User not found"));
            System.out.println(jwtUtil.validateRefreshToken(refreshToken, user));
            if(!jwtUtil.validateRefreshToken(refreshToken, user)) {
                throw new UserAuthException("Invalid token");
            }
            String accessToken = jwtUtil.generateToken(user.getEmail());
            revokeUserTokens(user);
            Token saveToken = Token.builder()
                    .user(user)
                    .token(accessToken)
                    .tokenType(TokenType.BEARER)
                    .revoked(false)
                    .expired(false)
                    .build();

            tokenRepository.save(saveToken);
            return AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        return null;
    }
}
