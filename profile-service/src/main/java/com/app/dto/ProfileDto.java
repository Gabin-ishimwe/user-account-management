package com.app.dto;

import com.app.entities.Gender;
import com.app.validations.maritalStatusEnumValidation.MaritalStatusEnumValidator;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDto {

    private String firstName;

    private String lastName;

    private MultipartFile profilePhoto;

    private int age;

    @MaritalStatusEnumValidator(enumC = Gender.class, message = "Gender must be MALE or FEMALE")
    private String gender;


    @MaritalStatusEnumValidator(message = "Marital status must be SINGLE, MARRIED, DIVORCED, WIDOWED")
    private String maritalStatus;

    private String nationality;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "Date format should be dd/MM/yyyy")
    private String dateOfBirth;
}
