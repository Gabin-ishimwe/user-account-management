package com.app.dto;

import com.app.validations.nationNumberValidation.IsNationalNumber;
import com.app.validations.passportNumberValidation.IsPassportNumber;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  AccountDto {
    @IsPassportNumber(message = "Invalid passport number")
    private String passportNumber;

    @IsNationalNumber
    private String nationalId;

    private MultipartFile document;

}
