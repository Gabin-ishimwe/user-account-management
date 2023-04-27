package com.app.services;

import com.app.dto.AuthResponseDto;
import com.app.dto.SignInDto;
import com.app.dto.SignupDto;
import com.app.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public AuthResponseDto userSignUp(SignupDto signupDto) {
        return null;
    }

    public AuthResponseDto userSignIn(SignInDto signInDto){
        return null;
    }
}
