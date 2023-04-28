package com.app.controllers;

import com.app.dto.AuthResponseDto;
import com.app.dto.SignInDto;
import com.app.dto.SignupDto;
import com.app.event.RegistrationEvent;
import com.app.exceptions.UserExistsException;
import com.app.services.UserService;
import com.app.utils.ApplicationUrl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    private final ApplicationUrl applicationUrl;

    @PostMapping(path = "/signup")
    public AuthResponseDto userSignUp(@RequestBody @Valid SignupDto signupDto, HttpServletRequest request) throws UserExistsException {
        AuthResponseDto responseDto =  userService.userSignUp(signupDto);
        publisher.publishEvent(new RegistrationEvent(responseDto, applicationUrl.applicationUrl(request)));
        return responseDto;
    }

    @PostMapping(path = "/verify-email")
    public String verifyEmail(@RequestParam("token") String token) {
        return userService.verifyUser(token);
    }

    @PostMapping(path = "/signin")
    public AuthResponseDto userLogin(@RequestBody @Valid SignInDto signInDto) {
        return userService.userSignIn(signInDto);
    }

    @GetMapping(path = "/user")
    public String getUser() {
        return "All users";
    }
}
