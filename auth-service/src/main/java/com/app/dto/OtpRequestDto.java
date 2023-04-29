package com.app.dto;

import com.app.validation.phoneNumberValidation.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class OtpRequestDto {
    @ValidPhoneNumber
    private String phoneNumber;
}
