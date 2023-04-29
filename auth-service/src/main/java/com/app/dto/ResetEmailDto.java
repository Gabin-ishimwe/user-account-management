package com.app.dto;

import com.app.validation.passwordValidation.Password;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetEmailDto {
    @Email
    private String email;


    @Password
    private String newPassword;
}
