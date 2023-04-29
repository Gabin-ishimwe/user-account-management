package com.app.controllers;

import com.app.dto.AuthResponseDto;
import com.app.dto.ResetEmailDto;
import com.app.dto.SignInDto;
import com.app.dto.SignupDto;
import com.app.event.registration.RegistrationEvent;
import com.app.exceptions.NotFoundException;
import com.app.exceptions.UserAuthException;
import com.app.exceptions.UserExistsException;
import com.app.services.UserService;
import com.app.services.VerificationTokenService;
import com.app.utils.ApplicationUrl;
import com.app.validation.passwordValidation.Password;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
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
    public String resetPasswordRequest(@RequestBody @Valid ResetEmailDto resetEmailDto, HttpServletRequest request) throws NotFoundException {
        System.out.println(resetEmailDto.getEmail());
        String url = applicationUrl.applicationUrl(request);
        return userService.forgotPasswordRequest(resetEmailDto.getEmail(), url);
    }

    @PostMapping(path = "/reset-password")
    public String resetPassword(@RequestParam("token") String token, @RequestBody @Valid ResetEmailDto resetEmailDto) throws NotFoundException {
        return userService.userResetPassword(token, resetEmailDto.getNewPassword());
    }

    @GetMapping(path = "/user")
    public String getUser() {
        return "All users";
    }
}
