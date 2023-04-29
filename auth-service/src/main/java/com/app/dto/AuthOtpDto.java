package com.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthOtpDto {
    @NotBlank(message = "Otp is required")
    private String otp;
}
