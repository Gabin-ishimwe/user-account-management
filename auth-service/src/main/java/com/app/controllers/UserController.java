package com.app.controllers;

import com.app.dto.AuthResponseDto;
import com.app.dto.SignInDto;
import com.app.dto.SignupDto;
import com.app.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@RequiredArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping(path = "/signup")
    public AuthResponseDto userSignUp(@RequestBody @Valid SignupDto signupDto) {
        return userService.userSignUp(signupDto);
    }

    @PostMapping(path = "/signin")
    public AuthResponseDto userLogin(@RequestBody @Valid SignInDto signInDto) {
        return userService.userSignIn(signInDto);
    }
}
