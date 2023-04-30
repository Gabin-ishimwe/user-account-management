package com.app.controllers;

import com.app.dto.*;
import com.app.event.registration.RegistrationEvent;
import com.app.exceptions.NotFoundException;
import com.app.exceptions.UserAuthException;
import com.app.exceptions.UserExistsException;
import com.app.services.UserService;
import com.app.services.VerificationTokenService;
import com.app.utils.ApplicationUrl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    private final ApplicationUrl applicationUrl;

    private final VerificationTokenService verificationTokenService;

    @PostMapping(path = "/sign-up")
    public ResponseEntity<AuthResponseDto> userSignUp(@RequestBody @Valid SignupDto signupDto, HttpServletRequest request) throws UserExistsException {
        AuthResponseDto responseDto =  userService.userSignUp(signupDto);
        publisher.publishEvent(new RegistrationEvent(responseDto, applicationUrl.applicationUrl(request)));
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping(path = "/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) throws Exception {
        return ResponseEntity.ok(verificationTokenService.verifyUser(token));
    }

    @PostMapping(path = "/sign-in")
    public ResponseEntity<AuthResponseDto> userLogin(@RequestBody @Valid SignInDto signInDto) throws UserAuthException {
        return ResponseEntity.ok(userService.userSignIn(signInDto));
    }

    @PostMapping(path = "/forgot-password")
    public String resetPasswordRequest(@RequestBody @Valid ResetLinkDto resetLinkDto, HttpServletRequest request) throws NotFoundException {
        System.out.println(resetLinkDto.getEmail());
        String url = applicationUrl.applicationUrl(request);
        return userService.forgotPasswordRequest(resetLinkDto.getEmail(), url);
    }

    @PostMapping(path = "/reset-password")
    public String resetPassword(@RequestParam("token") String token, @RequestBody @Valid ResetPasswordDto resetPasswordDto) throws NotFoundException {
        return userService.userResetPassword(token, resetPasswordDto.getPassword());
    }

    @PostMapping(path = "/enable-mfa")
    public String enableMfa(@RequestBody @Valid OtpRequestDto otpRequestDto) throws NotFoundException {
        return userService.enableMfa(otpRequestDto);
    }

    @PostMapping(path = "/send-otp")
    public String sendOtp(@RequestBody @Valid OtpRequestDto otpRequestDto) throws NotFoundException {
        return userService.userOtp(otpRequestDto);
    }

    @PostMapping(path = "/authenticate-otp")
    public AuthResponseDto authenticateOtp(@RequestBody @Valid AuthOtpDto authOtpDto) throws NotFoundException, UserAuthException {
        return userService.userValidateOtp(authOtpDto.getOtp());
    }

    @PostMapping(path = "/refresh-token")
    public AuthResponseDto refreshToken(HttpServletRequest request, HttpServletResponse response) throws UserAuthException {
        System.out.println("here");
        return userService.createRefreshToken(request, response);
    }

    @GetMapping(path = "/user")
    public String getUser() {
        return "All users";
    }
}
