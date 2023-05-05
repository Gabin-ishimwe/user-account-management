package com.app.dto;

import com.app.validation.phoneNumberValidation.ValidPhoneNumber;
import lombok.Data;

@Data
public class OtpRequestDto {
    @ValidPhoneNumber
    private String phoneNumber;
}
