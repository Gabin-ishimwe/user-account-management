package com.app.dto;

import com.app.entities.Gender;
import com.app.entities.MaritalStatus;
import com.app.validations.maritalStatusEnumValidation.MaritalStatusEnumValidator;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDto {

    private String firstName;

    private String lastName;

    private MultipartFile profilePhoto;

    private int age;

    @MaritalStatusEnumValidator(enumC = Gender.class)
    private String gender;


    @MaritalStatusEnumValidator()
    private String maritalStatus;

    private String nationality;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date format should be dd/MM/yyyy")
    private String dateOfBirth;
}
